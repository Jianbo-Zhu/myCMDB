import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './server.reducer';
import { IServer } from 'app/shared/model/server.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Server extends React.Component<IServerProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { serverList, match } = this.props;
    return (
      <div>
        <h2 id="server-heading">
          <Translate contentKey="myCmdbApp.server.home.title">Servers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="myCmdbApp.server.home.createLabel">Create new Server</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.hostname">Hostname</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.ipAddress">Ip Address</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.macAddress">Mac Address</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.position">Position</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.brand">Brand</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.memSize">Mem Size</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.coreNo">Core No</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.osVersion">Os Version</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.vendor">Vendor</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.purchaseDate">Purchase Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.warrantyDate">Warranty Date</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.dataCenter">Data Center</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.owner">Owner</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.server.vendorContact">Vendor Contact</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {serverList.map((server, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${server.id}`} color="link" size="sm">
                      {server.id}
                    </Button>
                  </td>
                  <td>{server.hostname}</td>
                  <td>{server.ipAddress}</td>
                  <td>{server.macAddress}</td>
                  <td>{server.position}</td>
                  <td>{server.brand}</td>
                  <td>{server.memSize}</td>
                  <td>{server.coreNo}</td>
                  <td>{server.osVersion}</td>
                  <td>{server.vendor}</td>
                  <td>
                    <TextFormat type="date" value={server.purchaseDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={server.warrantyDate} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{server.dataCenter ? <Link to={`data-center/${server.dataCenter.id}`}>{server.dataCenter.dcName}</Link> : ''}</td>
                  <td>{server.owner ? <Link to={`contactor/${server.owner.id}`}>{server.owner.name}</Link> : ''}</td>
                  <td>
                    {server.vendorContact ? <Link to={`contactor/${server.vendorContact.id}`}>{server.vendorContact.name}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${server.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${server.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${server.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ server }: IRootState) => ({
  serverList: server.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Server);
