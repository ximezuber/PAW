import React, {Component} from 'react';
import {Card} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

class GenericCard extends Component {
    render() {
        return (
            <Card className="mb-3" style={{color: "#000"}}>
                <Card.Img variant="top" src={"http://localhost:3000/" + this.props.src} />
                <Card.Body>
                    <Card.Title>{this.props.title}</Card.Title>
                    <Card.Text>
                        {this.props.text}
                    </Card.Text>
                    {this.props.children}
                </Card.Body>
            </Card>
        )
    }
}

export default GenericCard