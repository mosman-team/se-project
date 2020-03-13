export default class User {
    constructor(username, email, password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = []
    }
    addRole(role){
        this.role.push(role);
    }
}