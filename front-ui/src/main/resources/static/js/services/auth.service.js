import axios from 'axios';

const API_URL = 'http://localhost:8762/auth/'; // если я делаю запрос на регистрацию через gateway то он выдает cors ошибку
                                                // тут gateway отправляет на auth server

//const API_URL = 'http://localhost:8082/auth/'; // а если сразу на auth server то нет никаких ошибок

class AuthService {
    register(user) {
        return axios.post(API_URL + 'signup', {
            username: user.username,
            email: user.email,
            password: user.password,
            role : user.role
        });
    }

    login(user) {
        return axios
            .post(API_URL + 'signin', {
                username: user.username,
                password: user.password
            })
            .then(response => {
                if (response.data.accessToken) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }
}
export default new AuthService();









