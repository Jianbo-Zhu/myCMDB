import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './component-entity.reducer';
import { IComponentEntity } from 'app/shared/model/component-entity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IComponentEntityProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ComponentEntity extends React.Component<IComponentEntityProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { componentEntityList, match } = this.props;
    return (
      <div>
        <h2 id="component-entity-heading">
          <Translate contentKey="myCmdbApp.componentEntity.home.title">Component Entities</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="myCmdbApp.componentEntity.home.createLabel">Create new Component Entity</Translate>
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
                  <Translate contentKey="myCmdbApp.componentEntity.comName">Com Name</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.componentEntity.comType">Com Type</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.componentEntity.app">App</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.componentEntity.server">Server</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {componentEntityList.map((componentEntity, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${componentEntity.id}`} color="link" size="sm">
                      {componentEntity.id}
                    </Button>
                  </td>
                  <td>{componentEntity.comName}</td>
                  <td>
                    <Translate contentKey={`myCmdbApp.ComponentType.${componentEntity.comType}`} />
                  </td>
                  <td>
                    {componentEntity.app ? <Link to={`application/${componentEntity.app.id}`}>{componentEntity.app.appName}</Link> : ''}
                  </td>
                  <td>
                    {componentEntity.server ? (
                      <Link to={`server/${componentEntity.server.id}`}>{componentEntity.server.hostname}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${componentEntity.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${componentEntity.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${componentEntity.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ componentEntity }: IRootState) => ({
  componentEntityList: componentEntity.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ComponentEntity);
