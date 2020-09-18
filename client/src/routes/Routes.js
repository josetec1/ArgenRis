import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import HomePage from '../views/login-page';
import AppLayout from '../views/main-page';
import ProtectedRoute from '../Routes/protected-route';
const Routes = () => {
    return (
        <BrowserRouter>
            <Switch>
                <Route exact path='/' component={HomePage} />
                <ProtectedRoute exact path='/app' component={AppLayout} />
                <Route path='*' component={() => '404 NOT FOUND'} />
            </Switch>
        </BrowserRouter>
    );
}

export default Routes;