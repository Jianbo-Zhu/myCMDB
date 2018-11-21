import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './version.reducer';
import { IVersion } from 'app/shared/model/version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVersionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Version extends React.Component<IVersionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { versionList, match } = this.props;
    return (
      <div>
        <h2 id="version-heading">
          <Translate contentKey="myCmdbApp.version.home.title">Versions</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />&nbsp;
            <Translate contentKey="myCmdbApp.version.home.createLabel">Create new Version</Translate>
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
                  <Translate contentKey="myCmdbApp.version.versionString">Version String</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.deployedBy">Deployed By</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.gitCommit">Git Commit</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.gitCommitter">Git Committer</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.majorVersion">Major Version</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.minorVersion">Minor Version</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.hotfixNumber">Hotfix Number</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.buildNumber">Build Number</Translate>
                </th>
                <th>
                  <Translate contentKey="myCmdbApp.version.comp">Comp</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {versionList.map((version, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${version.id}`} color="link" size="sm">
                      {version.id}
                    </Button>
                  </td>
                  <td>{version.versionString}</td>
                  <td>{version.deployedBy}</td>
                  <td>{version.gitCommit}</td>
                  <td>{version.gitCommitter}</td>
                  <td>{version.majorVersion}</td>
                  <td>{version.minorVersion}</td>
                  <td>{version.hotfixNumber}</td>
                  <td>{version.buildNumber}</td>
                  <td>{version.comp ? <Link to={`component-entity/${version.comp.id}`}>{version.comp.comName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${version.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${version.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${version.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ version }: IRootState) => ({
  versionList: version.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Version);
