import React from 'react'
import { object } from 'prop-types';
import Auth from '../components/auth';
import SinginAside from '../components/sing-in/sing-in-aside'
const HomePage = props => {
    const { history } = props;
    return (
        <div>
            <SinginAside onSinginClicked={() => Auth.login(() => {
                    history.push('/app');
                })}/>
        </div>
    )
}

HomePage.propTypes = {
    history: object,
}

export default HomePage ;