import { sign, verify } from 'jsonwebtoken';

interface Token {
  uuid: string
}

class jsonwebtoken {
  static sign(user: Token, secret: string) {
    const payload = {
      uuid: user.uuid
    };

    return sign(payload, secret, {
      expiresIn: '8h',
    });
  }

  static verify(token: string, secret: string) {
    try {
      const decode = verify(token, secret) as Token;

      return {
        success: true,
        message: '',
        uuid: decode.uuid
      };
    } catch (err) {
      return {
        success: false,
        message: err.message,
      };
    }
  }
}

export default jsonwebtoken;
