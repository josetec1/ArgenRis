import React from 'react';
import { Navigate } from 'react-router-dom';
import MedicoDashboardLayout from 'src/layouts/MedicoDashboardLayout';
import PacienteDashboardLayout from 'src/layouts/PacienteDashboardLayout';
import MainLayout from 'src/layouts/MainLayout';
import AccountView from 'src/views/account/AccountView';
import MedicoPacientesView from 'src/views/Pacientes/MedicoPacientesView';
import PacienteOrdenesView from 'src/views/Ordenes/PacienteOrdenesView';
import MedicoOrdenesView from 'src/views/Ordenes/MedicoOrdenesView';
import CitasView from 'src/views/Citas/CitasView';
import LoginView from 'src/views/auth/LoginView';
import NotFoundView from 'src/views/errors/NotFoundView';
import ProductListView from 'src/views/product/ProductListView';
import RegisterView from 'src/views/auth/RegisterView';
import SettingsView from 'src/views/settings/SettingsView';
import ProtectedRoute from './protected-route';

const routes = [
  {
    path: 'medico',
    element: <ProtectedRoute rol='medico' component={MedicoDashboardLayout} />,
    children: [
      { path: '/', element: <ProtectedRoute rol='medico' component={MedicoOrdenesView} /> },
      { path: 'account', element: <ProtectedRoute rol='medico' component={AccountView} /> },
      { path: 'pacientes', element: <ProtectedRoute rol='medico' component={MedicoPacientesView} /> },
      { path: 'ordenes', element: <ProtectedRoute rol='medico' component={MedicoOrdenesView} /> },
      { path: 'products', element: <ProtectedRoute rol='medico' component={ProductListView} /> },
      { path: 'settings', element: <ProtectedRoute rol='medico' component={SettingsView} /> },
      { path: '*', element: <Navigate to='/404' /> }
    ]
  },
  {
    path: 'paciente',
    element: <ProtectedRoute rol='paciente' component={PacienteDashboardLayout} />,
    children: [
      { path: '/', element: <ProtectedRoute rol='paciente' component={PacienteOrdenesView} /> },
      { path: 'historialClinico', element: <ProtectedRoute rol='paciente' component={PacienteOrdenesView} /> },
      { path: 'historialDeCitas', element: <ProtectedRoute rol='paciente' component={CitasView} /> },
      { path: 'historialDeCitas/:citas', element: <ProtectedRoute rol='paciente' component={CitasView} /> },
      { path: '*', element: <Navigate to='/404' /> }
    ]
  },
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: 'login', element: <LoginView /> },
      { path: 'register', element: <RegisterView /> },
      { path: '404', element: <NotFoundView /> },
      { path: '/', element: <Navigate to='/login' /> },
      { path: '*', element: <Navigate to='/404' /> }
    ]
  }
];

export default routes;
