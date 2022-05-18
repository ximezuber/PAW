import React, {Component} from 'react';
import {Container, Row, Col} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import GenericCard from "./GenericCard";

class Cards extends Component {
    cardStructure;

    constructor(props) {
        super(props);
        this.cardStructure = this.listToMatrix(this.props.cards, this.props.cardsPerRow)
    }

    render() {
        return (
            <>
                <Container>
                    {this.cardStructure.map((cardRow) => {
                        return (
                            <Row>
                                {cardRow.map((card) => {
                                    return (
                                        <Col>
                                            <GenericCard
                                                src={card.src}
                                                text={card.text}
                                                title={card.title}
                                                children={card.children[0]}
                                            />
                                        </Col>
                                    )
                                })}
                            })
                            </Row>
                            )
                    })}
                </Container>
            </>
        )
    }

    listToMatrix = (list, elementsPerSubArray) => {
        let matrix = [], i, k;

        for (i = 0, k = -1; i < list.length; i++) {
            if (i % elementsPerSubArray === 0) {
                k++;
                matrix[k] = [];
            }

            matrix[k].push(list[i]);
        }

        return matrix;
    }
}

export default Cards