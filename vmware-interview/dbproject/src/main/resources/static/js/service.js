/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */
'use strict';



class Service extends React.Component {
    constructor(props) {
        super(props);
        this.state = { services: [] };
    }

    componentDidMount() {
        axios.get(`/subscribe/services`)
            .then(res => {
                const services = res.data; //res.data.data.children.map(obj => obj.data);
                this.setState({ services });
            });
    }

    render() {
        return (

            <div className="container">
            <div><h3>List All Services</h3></div>
            <table className='table table-striped table-bordered table-hover'>
                <thead>
                    <tr>
                        <th>Service ID</th>
                        <th>Service Name</th>
                        <th>Service Info</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.services.map(service=> {
                        return (
                        <tr key={service.id}>
                            <td>{service.id}</td>
                            <td>{service.serviceName}</td>
                            <td>{service.serviceInfo}</td>
                            <td></td>
                        </tr>)
                        })}
                </tbody>
        </table>
        </div>
    );

    }
}
const serviceContainer = document.querySelector('#service_container');
ReactDOM.render(React.createElement(Service), serviceContainer);
