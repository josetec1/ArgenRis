import React from 'react';
import { Navigate } from 'react-router-dom';
import { func, string } from 'prop-types';
import Auth from './components/auth';

const ProtectedRoute = ({ component: Component, rol }) => Auth.isAuthenticated() && Auth.isRol(rol) ? <Component /> : <Navigate to='/login' />;

ProtectedRoute.propTypes = {
  component: func,
  rol: string
};

export default ProtectedRoute;
