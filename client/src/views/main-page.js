import React from 'react';
import { object } from 'prop-types';
import Dashboard from '../components/dashboard'

const AppLayout = props => {
    return (
        <div>
            <Dashboard {...props} />
        </div>
    );
}

AppLayout.propTypes = {
    history: object,
}
export default AppLayout;