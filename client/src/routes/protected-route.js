import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { func, object } from 'prop-types';
import Auth from '../components/auth';
const ProtectedRoute = ({ component: Component, ...rest }) => {
    return (
        <Route
            {...rest}
            render={props => {
                const { location } = props;
                if(Auth.isAuthenticated()) {
                    return <Component {...props}/>
                }
                else {
                    return (
                        <Redirect
                            to={{
                                    pathname: "/",
                                    state: {
                                        from: location
                                    }
                                }}
                        />
                    )
                }
            }}
        />
    )
}

ProtectedRoute.propTypes = {
    component: func,
    location: object
}


export default ProtectedRoute;