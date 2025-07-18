import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    usePostLoginMutation,
    useGetDisplaytextsQuery,
} from '../api';
import LoginMessage from './LoginMessage';

export default function Login() {
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const basename = useSelector(state => state.basename);
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = {} } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });
    const { data: text = [] } = useGetDisplaytextsQuery(locale, { skip: !defaultLocaleIsSuccess });
    const [ postLogin ] = usePostLoginMutation();
    const onLoginClicked = async () => { await postLogin({ username, password: btoa(password), locale }) }

    return (
        <div className="Login">
            <header>
                <div className="pb-2 mt-4 mb-2 border-bottom bg-light">
                    <h1>Ratatoskr login</h1>
                    <p id="messagebanner"></p>
                </div>
            </header>
            <div className="container">
                <LoginMessage/>
                <form onSubmit={e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <label htmlFor="username" className="col-form-label col-3 mr-2">{text.username}:</label>
                        <div className="col-8">
                            <input
                                id="username"
                                className="form-control"
                                type="text" name="username"
                                value={username}
                                onChange={e => setUsername(e.target.value)} />
                        </div>
                    </div>
                    <div className="form-group row">
                        <label htmlFor="password" className="col-form-label col-3 mr-2">{text.password}:</label>
                        <div className="col-8">
                            <input
                                id="password" className="form-control"
                                type="password"
                                name="password"
                                value={password}
                                onChange={e => setPassword(e.target.value)}/>
                        </div>
                    </div>
                    <div className="form-group row">
                        <div className="offset-xs-3 col-xs-9">
                            <input className="btn btn-primary" type="submit" value="Login" onClick={onLoginClicked}/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}
