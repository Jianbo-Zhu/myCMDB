import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './server.reducer';
import { IServer } from 'app/shared/model/server.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServerDetail extends React.Component<IServerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serverEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="myCmdbApp.server.detail.title">Server</Translate> [<b>{serverEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="hostname">
                <Translate contentKey="myCmdbApp.server.hostname">Hostname</Translate>
              </span>
            </dt>
            <dd>{serverEntity.hostname}</dd>
            <dt>
              <span id="ipAddress">
                <Translate contentKey="myCmdbApp.server.ipAddress">Ip Address</Translate>
              </span>
            </dt>
            <dd>{serverEntity.ipAddress}</dd>
            <dt>
              <span id="macAddress">
                <Translate contentKey="myCmdbApp.server.macAddress">Mac Address</Translate>
              </span>
            </dt>
            <dd>{serverEntity.macAddress}</dd>
            <dt>
              <span id="position">
                <Translate contentKey="myCmdbApp.server.position">Position</Translate>
              </span>
            </dt>
            <dd>{serverEntity.position}</dd>
            <dt>
              <span id="brand">
                <Translate contentKey="myCmdbApp.server.brand">Brand</Translate>
              </span>
            </dt>
            <dd>{serverEntity.brand}</dd>
            <dt>
              <span id="memSize">
                <Translate contentKey="myCmdbApp.server.memSize">Mem Size</Translate>
              </span>
            </dt>
            <dd>{serverEntity.memSize}</dd>
            <dt>
              <span id="coreNo">
                <Translate contentKey="myCmdbApp.server.coreNo">Core No</Translate>
              </span>
            </dt>
            <dd>{serverEntity.coreNo}</dd>
            <dt>
              <span id="osVersion">
                <Translate contentKey="myCmdbApp.server.osVersion">Os Version</Translate>
              </span>
            </dt>
            <dd>{serverEntity.osVersion}</dd>
            <dt>
              <span id="vendor">
                <Translate contentKey="myCmdbApp.server.vendor">Vendor</Translate>
              </span>
            </dt>
            <dd>{serverEntity.vendor}</dd>
            <dt>
              <span id="purchaseDate">
                <Translate contentKey="myCmdbApp.server.purchaseDate">Purchase Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={serverEntity.purchaseDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="warrantyDate">
                <Translate contentKey="myCmdbApp.server.warrantyDate">Warranty Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={serverEntity.warrantyDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="myCmdbApp.server.dataCenter">Data Center</Translate>
            </dt>
            <dd>{serverEntity.dataCenter ? serverEntity.dataCenter.dcName : ''}</dd>
            <dt>
              <Translate contentKey="myCmdbApp.server.owner">Owner</Translate>
            </dt>
            <dd>{serverEntity.owner ? serverEntity.owner.name : ''}</dd>
            <dt>
              <Translate contentKey="myCmdbApp.server.vendorContact">Vendor Contact</Translate>
            </dt>
            <dd>{serverEntity.vendorContact ? serverEntity.vendorContact.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/server" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/server/${serverEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ server }: IRootState) => ({
  serverEntity: server.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServerDetail);
