import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Web3j와 관련된 Bean을 설정하는 Spring Configuration 클래스.
 * Web3j, Credentials 및 Web3Provider를 설정하여 이더리움과의 상호작용을 지원합니다.
 */
@Configuration
public class Web3Config {
    // application.properties 또는 application.yml 파일에서 설정 값을 읽어옵니다.
    @Value("${web3.wallet}")
    private String walletAddress;

    @Value("${web3.key}")
    private String privateKey;

    @Value("${web3.besu.url}")
    private String besuUrl;

    @Value("${backend_nft}")
    private String backendNft;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(besuUrl));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }

    @Bean
    public Web3Provider web3Provider(Web3j web3j, Credentials credentials) {
        return new Web3Provider(besuUrl, web3j, credentials, walletAddress, this.backendNft);
    }
}
