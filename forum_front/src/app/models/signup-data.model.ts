export class SignupData {
    username: string;
    password: string;
    email: string;
  
    constructor(username: string, password: string, email: string) {
      this.username = username;
      this.password = password;
      this.email = email;
    }
  }