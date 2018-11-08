import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './data-center.reducer';
import { IDataCenter } from 'app/shared/model/data-center.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDataCenterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DataCenterDetail extends React.Component<IDataCenterDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { dataCenterEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="myCmdbApp.dataCenter.detail.title">DataCenter</Translate> [<b>{dataCenterEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dcName">
                <Translate contentKey="myCmdbApp.dataCenter.dcName">Dc Name</Translate>
              </span>
            </dt>
            <dd>{dataCenterEntity.dcName}</dd>
            <dt>
              <span id="address">
                <Translate contentKey="myCmdbApp.dataCenter.address">Address</Translate>
              </span>
            </dt>
            <dd>{dataCenterEntity.address}</dd>
            <dt>
              <Translate contentKey="myCmdbApp.dataCenter.contractor">Contractor</Translate>
            </dt>
            <dd>{dataCenterEntity.contractor ? dataCenterEntity.contractor.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/data-center" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/data-center/${dataCenterEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ dataCenter }: IRootState) => ({
  dataCenterEntity: dataCenter.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataCenterDetail);
