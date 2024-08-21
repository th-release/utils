import lombok.Getter;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;
import project.contract.Sol;
import project.utils.Failable;
import project.utils.web3.responses.GetRemainGas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Web3Provider {
    private String besuURL;
    private Web3j web3j;
    //
    private Credentials credentials;

    @Getter
    private String walletAddress;

    @Getter
    private String backendNft;

    private BigInteger gasLimit = BigInteger.valueOf(50000000);

    private ContractGasProvider gasProvider =
    new StaticGasProvider(
            BigInteger.valueOf(20000000000L),
            gasLimit
    );

    public Web3Provider(
            String besuURL,
            Web3j web3j,
            Credentials credentials,
            String walletAddress,
            String backendNft
    ) {
        this.besuURL = besuURL;
        this.web3j = web3j;
        this.credentials = credentials;
        this.walletAddress = walletAddress;
        this.backendNft = backendNft;

        try {
            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                    .send()
                    .getBlock();

            gasLimit = latestBlock.getGasLimit();

            gasProvider = new StaticGasProvider(
                    BigInteger.valueOf(20000000000L),
                    gasLimit
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public Failable<GetRemainGas, String> getGasRemainData(String hash) {
        try {
            EthGetTransactionReceipt response = web3j.ethGetTransactionReceipt(hash).send();

            if (response.getTransactionReceipt().isEmpty()) {
                return Failable.error("Transaction receipt not found.");
            }

            TransactionReceipt receipt = response.getTransactionReceipt().get();

            long gasUsed = receipt.getGasUsed().longValue();

            return Failable.success(
                    GetRemainGas.builder()
                            .gasUsed(gasUsed)
                            .gasLimit(gasLimit.longValue())
                            .remainGas(gasLimit.longValue() - gasUsed)
                            .build()
            );
        } catch (Exception e) {
            return Failable.error(e.getMessage());
        }
    }

    public String hashMessage(String message) {
        return Hash.sha3(
                Numeric.toHexStringNoPrefix(
                        ("\u0019Ethereum Signed Message:\n" + message.length() + message).getBytes(StandardCharsets.UTF_8)));
    }

    // 주어진 메시지의 해시(digest)와 서명(signature)을 사용하여 서명한 사람의 이더리움 주소를 복구합니다.
    // 해당 서명이 유효하면 공개 주소를 반환하고, 유효하지 않을경우 null
    public String recoverAddress(String digest, String signature) {
        // 서명 문자열에서 SignatureData 객체를 생성
        Sign.SignatureData signatureData = this.getSignatureData(signature);
        int header = 0;

        // 서명의 V 값을 바이트 배열로부터 정수로 변환
        for (byte b : signatureData.getV()) {
            //  이 연산은 header 값을 8비트(1바이트) 왼쪽으로 시프트합니다. 이는 다음 바이트를 추가하기 전에 현재까지의 값을 왼쪽으로 이동시키는 것입니다.
            // b의 하위 8비트만을 추출하기 위한 것입니다.
            header = (header << 8) + (b & 0xFF);
        }

        // V 값이 유효한 범위(27~34)에 있는지 확인
        // 표준 이더리움 서명에서 V 값은 27 또는 28
        // EIP-155라는 이더리움 개선 제안이 도입되면서 특정 체인 ID를 가진 트랜잭션에 대해 V 값이 27보다 큰 값을 가질 수 있습니다.
        if (header < 27 || header > 34) {
            return null; // 유효하지 않은 서명이면 null 반환
        }

        // v 값이 서명을 복구하는데 필요한 복구 아이디 (recId)를 나타내기 때문 (v 값은 27 또는 28 을 가지기 때문)
        int recId = header - 27;

        // 서명에서 공개 키를 복구
        BigInteger key = Sign.recoverFromSignature(
                recId,
                new ECDSASignature(
                        new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                Numeric.hexStringToByteArray(digest));

        if (key == null)
            return null; // 공개 키를 복구할 수 없으면 null 반환

        return ("0x" + Keys.getAddress(key)).trim();
    }

    public boolean verifyAccount(String account, String message, String signature) {
        return !this
                .recoverAddress(this.hashMessage(message), signature)
                .equalsIgnoreCase(account);
    }

    // ECDSA 서명에서 R, S, V 값을 추출하여 Sign.SignatureData 객체로 반환하는 메서드
    private Sign.SignatureData getSignatureData(String signature) {
        // 1. 서명 문자열을 바이트 배열로 변환
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

        // 2. 서명의 마지막 바이트인 V 값을 추출
        byte v = signatureBytes[64];
        if (v < 27) {
            // 3. V 값이 27보다 작다면, 표준 이더리움 서명 형식에 맞추기 위해 27을 더함
            v += 27;
        }
        // 4. 서명의 첫 번째 32바이트에서 R 값을 추출
        byte[] r = (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32);
        // 5. 서명의 두 번째 32바이트에서 S 값을 추출
        byte[] s = (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64);
        // 6. V, R, S 값을 사용하여 새로운 SignatureData 객체를 생성하고 반환
        return new Sign.SignatureData(v, r, s);
    }

    public Failable<Sol, String> deployContract() {
        try {
            Sol contract = Sol.deploy(
                    web3j, credentials, gasProvider, walletAddress
            ).send();

            return Failable.success(contract);
        } catch (Exception e) {
            System.out.println("Error deploying contract: " + e); // 자세한 로그 추가
            return Failable.error("Contract Error Exception: " + e.getMessage());
        }
    }

    public Failable<Boolean, String> verifyContract(Sol contract) {
        try {
            String owner = contract.owner().send();

            if (!owner.equalsIgnoreCase(credentials.getAddress()))
                return Failable.error("해당 서버에서 배포한 스마트 컨트랙트가 아닙니다.");

            return Failable.success(true);
        } catch (Exception e) {
            return Failable.error(e.getMessage());
        }
    }

    public Failable<Sol, String> load(String address) {
        try {
            Sol contract = Sol.load(
                    address,
                    web3j,
                    credentials,
                    gasProvider
            );

            Failable<Boolean, String> verify = verifyContract(contract);

            if (verify.isError())
                return Failable.error(verify.getError());

            return Failable.success(contract);
        } catch (Exception e) {
            return Failable.error(e.getMessage());
        }
    }

    public Failable<BigDecimal, String> getBalanceByWalletAddress(String address) {
        try {
            // 주어진 주소(address)에 대한 잔액을 조회합니다. DefaultBlockParameterName.LATEST는 최신 블록 상태를 기준으로 합니다.
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();

            // 조회한 잔액을 wei 단위로 가져옵니다. wei는 Ethereum의 가장 작은 단위입니다.
            BigInteger wei = ethGetBalance.getBalance();

            // wei 단위를 ether 단위로 변환합니다. 1 ether = 10^18 wei이므로, wei 값을 10^18으로 나눕니다.
            BigDecimal ether = new BigDecimal(wei).divide(new BigDecimal("1000000000000000000"));

            return Failable.success(ether);
        } catch (Exception e) {
            return Failable.error(e.getMessage());
        }
    }
}
