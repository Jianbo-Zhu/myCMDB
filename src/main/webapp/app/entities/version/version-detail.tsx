import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './version.reducer';
import { IVersion } from 'app/shared/model/version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VersionDetail extends React.Component<IVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { versionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="myCmdbApp.version.detail.title">Version</Translate> [<b>{versionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="versionString">
                <Translate contentKey="myCmdbApp.version.versionString">Version String</Translate>
              </span>
            </dt>
            <dd>{versionEntity.versionString}</dd>
            <dt>
              <span id="deployedBy">
                <Translate contentKey="myCmdbApp.version.deployedBy">Deployed By</Translate>
              </span>
            </dt>
            <dd>{versionEntity.deployedBy}</dd>
            <dt>
              <span id="gitCommit">
                <Translate contentKey="myCmdbApp.version.gitCommit">Git Commit</Translate>
              </span>
            </dt>
            <dd>{versionEntity.gitCommit}</dd>
            <dt>
              <span id="gitCommitter">
                <Translate contentKey="myCmdbApp.version.gitCommitter">Git Committer</Translate>
              </span>
            </dt>
            <dd>{versionEntity.gitCommitter}</dd>
            <dt>
              <span id="majorVersion">
                <Translate contentKey="myCmdbApp.version.majorVersion">Major Version</Translate>
              </span>
            </dt>
            <dd>{versionEntity.majorVersion}</dd>
            <dt>
              <span id="minorVersion">
                <Translate contentKey="myCmdbApp.version.minorVersion">Minor Version</Translate>
              </span>
            </dt>
            <dd>{versionEntity.minorVersion}</dd>
            <dt>
              <span id="hotfixNumber">
                <Translate contentKey="myCmdbApp.version.hotfixNumber">Hotfix Number</Translate>
              </span>
            </dt>
            <dd>{versionEntity.hotfixNumber}</dd>
            <dt>
              <span id="buildNumber">
                <Translate contentKey="myCmdbApp.version.buildNumber">Build Number</Translate>
              </span>
            </dt>
            <dd>{versionEntity.buildNumber}</dd>
            <dt>
              <span id="createdTime">
                <Translate contentKey="myCmdbApp.version.createdTime">Created Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={versionEntity.createdTime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="udpatedTime">
                <Translate contentKey="myCmdbApp.version.udpatedTime">Udpated Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={versionEntity.udpatedTime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="myCmdbApp.version.comp">Comp</Translate>
            </dt>
            <dd>{versionEntity.comp ? versionEntity.comp.comName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/version/${versionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ version }: IRootState) => ({
  versionEntity: version.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VersionDetail);
