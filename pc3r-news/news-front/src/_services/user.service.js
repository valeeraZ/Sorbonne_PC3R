import {URL} from '../_constants/config';
import {handleResponse} from '../_utils/ErrorResponseHandler';

export const userService = {
    register
}

function register(name, password, email){
    let formdata = new URLSearchParams();
    formdata.append("name", name);
    formdata.append("email", email);
    formdata.append("password", password);
    const request = {
        method: 'POST',
        headers: {'Content-Type':'application/x-www-form-urlencoded'},
        body: formdata
        //body: JSON.stringify({name, password, email})
    };

    return fetch(`${URL}/users`, request)
        .then(handleResponse);
}
