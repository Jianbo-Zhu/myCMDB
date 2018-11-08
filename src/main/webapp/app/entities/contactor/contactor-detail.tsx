import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contactor.reducer';
import { IContactor } from 'app/shared/model/contactor.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactorDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContactorDetail extends React.Component<IContactorDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contactorEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="myCmdbApp.contactor.detail.title">Contactor</Translate> [<b>{contactorEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="myCmdbApp.contactor.name">Name</Translate>
              </span>
            </dt>
            <dd>{contactorEntity.name}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="myCmdbApp.contactor.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{contactorEntity.phone}</dd>
          </dl>
          <Button tag={Link} to="/entity/contactor" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/contactor/${contactorEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ contactor }: IRootState) => ({
  contactorEntity: contactor.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactorDetail);
