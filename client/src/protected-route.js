import React from 'react';
import { Navigate } from 'react-router-dom';
import { func, string } from 'prop-types';
import AuthenticationService from './components/Authentication/AuthenticationService';

const ProtectedRoute = ({ component: Component, rol }) => AuthenticationService.isAuthenticated() && AuthenticationService.isRol(rol) ? <Component /> : <Navigate to='/login' />;

ProtectedRoute.propTypes = {
  component: func,
  rol: string
};

export default ProtectedRoute;
