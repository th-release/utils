import db from './database';
import jwt from 'jsonwebtoken'

export default class JwtFunctions {
  static sign(item: object, options: object): string {
    return jwt.sign(item, "SECRET", options);
  }

  static async verify(token: string | undefined) {
    if (!token) 
      return {
        data: {},
        status: false
      };
    const data = jwt.verify(token, "SECRET");
    if (!data)
      return {
        data: {},
        status: false
      };
    
    return {
      data,
      status: true
    };
  }
}
