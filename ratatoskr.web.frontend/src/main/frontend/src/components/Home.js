import React from 'react';
import { NavLink } from 'react-router';
import { useSelector, useDispatch } from 'react-redux';
import Container from './bootstrap/Container';
import { LOGOUT_REQUEST } from '../reduxactions';
import Locale from './Locale';
import ChevronLeft from './bootstrap/ChevronLeft';


export default function Home() {
    const text = useSelector(state => state.displayTexts);
    const loginresult = useSelector(state => state.loginresult);
    const accountCount = useSelector(state => state.accounts.length);
    const dispatch = useDispatch();
    const { username, firstname, lastname, email } = loginresult.user;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../..">
                    <ChevronLeft />&nbsp;{text.gohome}!
                </a>
                <h1>Ratatoskr</h1>
                <NavLink className="btn btn-primary" to="/counter">{text.counter}</NavLink>
                <Locale />
            </nav>
            <Container>
                <p>{text.hi} {firstname}!</p>
                <p>{text.numberofaccounts}: {accountCount}</p>
                <p>{text.logged_in_user_info}</p>
                <table className="table">
                    <tbody>
                        <tr>
                            <th>{text.username}</th>
                            <td>{username}</td>
                        </tr>
                        <tr>
                            <th>{text.firstname}</th>
                            <td>{firstname}</td>
                        </tr>
                        <tr>
                            <th>{text.lastname}</th>
                            <td>{lastname}</td>
                        </tr>
                        <tr>
                            <th>{text.email}</th>
                            <td>{email}</td>
                        </tr>
                    </tbody>
                </table>
                <p><button className="btn btn-primary" onClick={() => dispatch(LOGOUT_REQUEST())}>{text.logout}</button></p>
            </Container>
        </div>
    );
}
