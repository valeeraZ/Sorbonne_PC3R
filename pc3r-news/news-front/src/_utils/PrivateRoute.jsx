import React from 'react';
import {Route, Redirect} from 'react-router-dom';
import {authenticationService} from '../_services/authentication.service';

export const PrivateRoute = ({component: Component, ...rest}) => (
    <Route {...rest} render = {props => {
        const currentUser = authenticationService.currentUserValue;
        //not logged in, redirect to login
        if(!currentUser){
            return <Redirect to={ {pathname: '/login', state:{from: props.location} }} />
        }

        //authorized
        return <Component {...props} />
    }} />
)