/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */
'use strict';
var Navbar = ReactBootstrap.Navbar;


class Header extends React.Component {

    render() {
        return (
            <Navbar bg="light" expand="lg">
                <Navbar.Brand>DB Project</Navbar.Brand>
            </Navbar>
        );
    }
}

class Footer extends React.Component {
    render() {
        return (
        <footer className="footer navbar-inverse navbar-fixed-bottom">
            <div className="container">
                <div className="row">
                    <div className="col-md-4"></div>
                    <div className="col-md-4"><p className="text-muted">@ Copyright 2019 Matthew Jiang</p></div>
                    <div className="col-md-2"><a href="swagger-ui.html">API</a></div>
                    <div className="col-md-2"><a href="/actuator/health">Health Check</a></div>
                </div>
            </div>
        </footer>
        )
    }
}

const header_container = document.querySelector('#header_container');
ReactDOM.render(React.createElement(Header), header_container);

const footer_container = document.querySelector('#footer_container');
ReactDOM.render(React.createElement(Footer), footer_container);