/*
 * Copyright (c) 2019, Matthew Jiang. All rights reserved.
 */

'use strict';

var ButtonToolbar = ReactBootstrap.ButtonToolbar;
var Button = ReactBootstrap.Button;
var Modal = ReactBootstrap.Modal;
var Container = ReactBootstrap.Container;
var Form = ReactBootstrap.Form;
var Alert = ReactBootstrap.Alert;
React.__spread = Object.assign;


class DataSource {
    constructor() {
        this._event = {};
    }

    addChangeListener(eventName, handler) {
        let listeners = this._event[eventName];
        if (!listeners || !listeners.length) {
            this._event[eventName] = [handler];
            return;
        }
        listeners.push(handler);
    }

    removeChangeListener(eventName, handler) {
        let listeners = this._event[eventName];
        this._event[eventName] = listeners.filter(l => l !== handler);
    }

    dispatch(eventName, ...args) {
        const listeners = this._event[eventName];
        if(listeners && listeners.length) {
            for (const l of listeners) {
                l(...args);
            }
        }
    }
}

const dataSource = new DataSource();
const Event = {
    customerUpdate: 'customerUpdate'
};
/**
 * Available service list
 */
class ServiceSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    render() {
        /**
         * handler for service id changing
         * upstream changed service id to parent
         * @param event fired event
         */
        let serviceChanged = (event) => {
            const { updateServiceId } = this.props;
            updateServiceId(event.target.value);
        };

        const {services} = this.props;
        return (
            <Form.Control as="select"  onChange={serviceChanged}>
                {services.map(service=> {
                    return (
                        <option key={service.id} value={service.id}>{service.serviceName} </option>
                    )
                })}
            </Form.Control>
        )
    }

}

/**
 * Customer list
 */
class CustomerSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {   };
    }

    render() {

        /**
         * handler for customer id changing by select new customer
         * upstream changed customer id to parent
         * @param event changed customer id
         */
        let customerChanged = (event) => {
            const { updateCustomerId } = this.props;
            updateCustomerId(event.target.value);
        };

        const {customers} = this.props;
        return (
            <Form.Control as="select"  onChange={customerChanged}>
                {customers.map(customer=> {
                    return (
                        <option key={customer.id} value={customer.id}>{customer.customerName} </option>
                    )
                })}
            </Form.Control>
        )
    }
}

/**
 * Subscribe form, including list of customer and list of available services
 * select customer will also cause corresponding available services changed
 */
class SubscribeForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = { customerId: 0, customers: [], services : [] };
        this.updateCustomerId = this.updateCustomerId.bind(this);
        this.updateServiceId = this.updateServiceId.bind(this);
        // parent onUpdateAlert function, call this function to update alert message
        this.updateAlert = this.props.onUpdateAlert;
        // parent onUpdateId function, call this function when customer id or service id changed
        this.updateId = this.props.onUpdateId;
    }

    componentDidMount() {
        dataSource.addChangeListener(Event.customerUpdate, this.updateCustomerId);
        // Load all customers from server
        axios.get(`/subscribe/customers`)
            .then(res => {
                const customers = res.data; //res.data.data.children.map(obj => obj.data);
                const firstId = customers.length > 0 ? customers[0].id : 0;
                this.setState({ customers: customers, customerId: firstId, alertMessage: ''});
                // Update first customer's available services
                if (firstId > 0) {
                    this.updateCustomerId(firstId)
                }
            })
            .catch(error => {
                this.updateAlert('danger', 'get customer failed: ' + error.response.data.message);
            });
    }

    componentWillUnmount() {
        dataSource.removeChangeListener(Event.customerUpdate, this.updateCustomerId);
    }

    /**
     * call back function which will be called while customer selected value changed
     * get available service base on new customer id and update ui.
     */
    updateCustomerId(customerId) {
        if (customerId <= 0) {
            return;
        }
        axios.get('/subscribe/customers/' + customerId + '/available')
            .then(res => {
                const services = res.data;
                const firstId = services.length > 0 ? services[0].id : 0;
                this.setState({ services: services, alertMessage: '',customerId: customerId  });
                this.updateId(customerId, firstId);
            })
            .catch(error => {
                this.updateAlert('danger', 'get available services failed: ' + error.response.data.message);
        });
    }

    /**
     * call back function which will be called while service selected value changed
     *
     * @param serviceId
     */
    updateServiceId(serviceId) {
        this.updateId(this.state.customerId, serviceId);
    }

    render() {
        return (
            <Form>
                <Form.Group controlId="formCustomer">
                    <Form.Label>Customer</Form.Label>
                    <CustomerSelect customers={this.state.customers} updateCustomerId={this.updateCustomerId} />
                    <Form.Text className="text-muted">
                        Please select customer.
                    </Form.Text>
                </Form.Group>

                <Form.Group controlId="formService">
                    <Form.Label>Service</Form.Label>
                    <ServiceSelect services={this.state.services} updateServiceId={this.updateServiceId}/>
                    <Form.Text className="text-muted">
                        Please select service.
                    </Form.Text>
                </Form.Group>
            </Form>
        );
    }
}

/**
 * Subscribe service dialog
 */
class SubscribeModalDialog extends React.Component {
    constructor(props) {
        super(props);
        this.state = { customerId: 0, serviceId: 0, alertVariant: 'success', alertMessage: '' };
        this.updateId = this.updateId.bind(this);
        this.subscribeService = this.subscribeService.bind(this);
    }

    updateId(customerId, serviceId) {
        this.state.customerId = customerId;
        this.state.serviceId = serviceId;
    }

    updateAlert(variant, message) {
        this.setState({alertVariant: variant, alertMessage: message});
    }

    /**
     * Call rest service to subscribe service
     */
    subscribeService() {
        if(this.state.customerId <= 0 || this.state.serviceId <= 0)
        {
            this.updateAlert("warning", "Please select customer and service");
            return;
        }

        axios.post('/subscribe/' + this.state.serviceId + '/' + this.state.customerId)
            .then(res => {
                // TODO: update customer table and available services
                dataSource.dispatch(Event.customerUpdate, this.state.customerId);
                const customer = res.data;
                this.updateAlert('success', 'subscribe service succeed.');

            })
            .catch(error=>{
                this.updateAlert("danger", "subscribe service failed: " + error.response.data.message);
            });
    }

    render() {
        return (
            <Modal
                {...this.props}
                size="lg"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        Subscribe Service
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Alert variant={this.state.alertVariant}>
                        {this.state.alertMessage}
                    </Alert>
                    <SubscribeForm onUpdateId={this.updateId} onUpdateAlert={this.updateAlert}/>
                </Modal.Body>
                <Modal.Footer>
                    <Button onClick={this.props.onHide}>Close</Button>
                    <Button variant="primary" type="submit" onClick={this.subscribeService}>
                        Submit
                    </Button>
                </Modal.Footer>
            </Modal>
        );
    }
}

class Subscribe extends React.Component {
    constructor(props) {
        super(props);
        this.state = { result: false, modalShow: false };
    }

    render() {
        let modalClose = () => this.setState({ modalShow: false });
        return (
            <Container>
                <ButtonToolbar>
                    <Button
                        variant="primary"
                        onClick={() => this.setState({ modalShow: true })}
                    >
                        Subscribe Service
                    </Button>

                    <SubscribeModalDialog
                        show={this.state.modalShow}
                        onHide={modalClose}
                    />
                </ButtonToolbar>
            </Container>
        );
    }
}

const subscribeContainer = document.querySelector('#subscribe_container');
ReactDOM.render(React.createElement(Subscribe), subscribeContainer);
