import {BehaviorSubject} from 'rxjs';
import {URL} from '../_constants/config';
import {handleResponse} from '../_utils/ErrorResponseHandler';

const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));

export const authenticationService = {
    login,
    logout,
    currentUser: currentUserSubject.asObservable(),
    get currentUserSubject(){ return currentUserSubject },
    get currentUserValue(){ return currentUserSubject.value}
};

function login(email, password){
    let formdata = new URLSearchParams();
    formdata.append("email", email);
    formdata.append("password", password);
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type':'application/x-www-form-urlencoded'},
        body: formdata
        //body: JSON.stringify({email, password})
    }

    return fetch(`${URL}/authentication`, requestOptions)
        .then(handleResponse)
        .then(user => {
            //store user details in local strorage
            localStorage.setItem('currentUser',JSON.stringify(user));
            currentUserSubject.next(user);
            return user;
        })
}

function logout(){
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    currentUserSubject.next(null);
}