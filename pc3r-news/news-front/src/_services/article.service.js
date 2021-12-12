import {URL} from '../_constants/config';
import {handleResponse} from '../_utils/ErrorResponseHandler';
import {authenticationService} from './authentication.service';

export const articleService = {
    getArticles,
    getSpecifiedArticles
}

function getArticles(){
    const currentUser = authenticationService.currentUserValue;
    const id_user = currentUser.id_user;

    const url = `${URL}/articles`+"?id_user="+id_user;
    const token = localStorage.getItem('token');
    const request = {
        method: 'GET',
        headers: {'Content-Type':'application/x-www-form-urlencoded',
                  'Authorization':token},
    };

    return fetch(url, request).then(handleResponse);
}

function getSpecifiedArticles(){
    const url = `${URL}/specified-articles`
    const request = {
        method: 'GET',
        headers: {'Content-Type':'application/x-www-form-urlencoded'}
    };

    return fetch(url, request).then(handleResponse);
}