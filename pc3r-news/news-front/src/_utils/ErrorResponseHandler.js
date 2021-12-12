import {authenticationService} from '../_services/authentication.service';
export function handleResponse(response){
    if(response.headers.get('Authorization') !== null){
        localStorage.setItem('token', response.headers.get('Authorization'));
    }
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if(!response.ok){
            //unauthorized or forbidden
            if([401,403].indexOf(response.status) !== -1 && !response.url.includes("/authentication")){
                authenticationService.logout();
                window.location.reload(true);
            }
            return Promise.reject(data);
        }
        
        return data;
    })
}