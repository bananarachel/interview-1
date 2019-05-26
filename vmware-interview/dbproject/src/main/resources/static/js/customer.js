/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */
'use strict';
var Container = ReactBootstrap.Container;
var Table = ReactBootstrap.Table;

class CustomerService extends React.Component {
    constructor(props) {
        super(props);
        this.state = {  };
        this.handleUnsubscribeService = this.handleUnsubscribeService.bind(this);
    }

    handleUnsubscribeService(customerId, serviceId) {

        const {onUnsubscribe} = this.props;
        onUnsubscribe(customerId, serviceId);
    }

    render() {
        return (

            <div className="container">
                <div><h3>{this.props.title}</h3></div>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Service ID</th>
                        <th>Service Name</th>
                        <th>Service Info</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.props.services.map(service=> {
                        return (
                            <tr key={service.id}>
                                <td>{service.id}</td>
                                <td>{service.serviceName}</td>
                                <td>{service.serviceInfo}</td>
                                <td>
                                    <Button variant="outline-danger" size="sm"
                                            onClick={()=> this.handleUnsubscribeService(this.props.currentCustomerId, service.id)}>unsubscribe</Button>
                                </td>
                            </tr>)
                    })}
                    </tbody>
                </Table>
            </div>
        );

    }
}

class Customer extends React.Component {
    constructor(props) {
        super(props);
        this.state = { customers: [], services: [], currentCustomerId: 0, serviceTitle: '', alertVariant:'success', alertMessage:'' };
        this.showSubscribedService = this.showSubscribedService.bind(this);
        this.updateCustomerId = this.updateCustomerId.bind(this);
        this.unsubscribeService = this.unsubscribeService.bind(this);
        this.updateAlert = this.updateAlert.bind(this);
        this.updateCustomer = this.updateCustomer.bind(this);
    }

    updateCustomer(customer) {
        const customers = this.state.customers;
        for (let i = 0; i < customers.length; ++i) {
            if (customers[i].id === customer.id) {
                customers[i] = customer;
                break;
            }
        }
        if (this.state.currentCustomerId === customer.id) {
            let title = customer.customerName + "'s subscribed service:";
            this.setState({customers: customers, services: customer.subscribedService, serviceTitle: title, currentCustomerId: customer.id});
        } else {
            this.setState({customers: customers});
        }
    }

    unsubscribeService(customerId, serviceId) {
        if (customerId <= 0 || serviceId <= 0) {
            return;
        }

        axios.delete('subscribe/' + serviceId + '/' + customerId)
            .then(res=>{
                const customer = res.data;
                this.updateCustomer(customer);
                this.updateAlert('success', 'unsubscribe service succeed.');
            })
            .catch(error=>{
               this.updateAlert('danger', 'unsubscribe service failed: ' + error.response.data.message);
            });
    }

    updateAlert(variant, message) {
        this.setState({alertVariant: variant, alertMessage: message});
    }

    updateCustomerId(customerId) {
        axios.get('subscribe/customers/' + customerId)
            .then(res => {
                const customer = res.data;
                this.updateCustomer(customer);
            });

    }

    showSubscribedService(customerId) {
        let customer = this.state.customers.filter(c => c.id === customerId);
        if (customer && customer.length > 0) {
            let c = customer[0];
            let title = c.customerName + "'s subscribed service:";
            this.setState( {services: c.subscribedService, serviceTitle: title, currentCustomerId: customerId});
        }
    }
    componentDidMount() {
        dataSource.addChangeListener(Event.customerUpdate, this.updateCustomerId);
        axios.get(`/subscribe/customers`)
            .then(res => {
                const customers = res.data;
                if (customers && customers.length > 0) {
                    let customer = customers[0];
                    let title = customer.customerName + "'s subscribed service:";
                    this.setState({ customers: customers, services: customer.subscribedService, serviceTitle: title, currentCustomerId: customer.id });
                }
            });
    }

    componentWillUnmount() {
        dataSource.removeChangeListener(Event.customerUpdate, this.updateCustomerId);
    }

    render() {
        return (
            <Container>
                <Alert variant={this.state.alertVariant}>
                    {this.state.alertMessage}
                </Alert>
                <div><h3>All Customers</h3></div>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Customer ID</th>
                        <th>Customer Name</th>
                        <th>Customer Info</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.customers.map(customer=> {
                        return (
                            <tr  key={customer.id}>
                                <td>{customer.id}</td>
                                <td>{customer.customerName}</td>
                                <td>{customer.customerInfo}</td>
                                <td>
                                    <Button variant="outline-primary" size="sm"
                                            onClick={()=> this.showSubscribedService(customer.id)}>Subscribed Service</Button>
                                </td>
                            </tr>
                        )
                    })}
                    </tbody>
                </Table>
                <CustomerService services={this.state.services}
                                 title={this.state.serviceTitle}
                                 currentCustomerId={this.state.currentCustomerId}
                                 onUnsubscribe={this.unsubscribeService}
                />
            </Container>
        )
    }
}

const customerContainer = document.querySelector('#customer_container');
ReactDOM.render(React.createElement(Customer), customerContainer);

