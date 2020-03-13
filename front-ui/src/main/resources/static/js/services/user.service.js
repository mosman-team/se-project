import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8762/test/';

class UserService {
    getPublicContent() {
        return axios.get(API_URL + 'all');
    }

    getStudentBoard() {
        return axios.get(API_URL + 'student', { headers: authHeader() });
    }

    getTeacherBoard() {
        return axios.get(API_URL + 'teacher', { headers: authHeader() });
    }
}

export default new UserService();