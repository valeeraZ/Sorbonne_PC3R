import {URL} from '../_constants/config';
import {handleResponse} from '../_utils/ErrorResponseHandler';
import {authenticationService} from './authentication.service';

export const subscriptionService = {
    getCategories,
    getSubscriptions,
    addSubscriptions
}

function getCategories(){
    const request = {
        method: 'GET'
    };

    return fetch(`${URL}/categories`, request).then(handleResponse);
}

function getSubscriptions(){
    const currentUser = authenticationService.currentUserValue;
    return currentUser.subscriptions;
}

function addSubscriptions(categories){
    const currentUser = authenticationService.currentUserValue;
    const id_user = currentUser.id_user;
    let formdata = new URLSearchParams();
    formdata.append("id_user", id_user)
    categories.forEach((index, value, array) => {
        formdata.append("c"+(parseInt(value)+1), index)
    })
    console.log(formdata.toString())
    const token = localStorage.getItem('token')
    const request = {
        method: 'POST',
        headers: {'Content-Type':'application/x-www-form-urlencoded',
                  'Authorization':token},
        body: formdata
    };

    return fetch(`${URL}/subscriptions`, request)
        .then(handleResponse)
        .then(user => {
            localStorage.setItem('currentUser',JSON.stringify(user));
            authenticationService.currentUserSubject.next(user);
            return user;
        });

}