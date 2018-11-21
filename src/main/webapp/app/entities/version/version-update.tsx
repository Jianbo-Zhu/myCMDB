import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IComponentEntity } from 'app/shared/model/component-entity.model';
import { getEntities as getComponentEntities } from 'app/entities/component-entity/component-entity.reducer';
import { getEntity, updateEntity, createEntity, reset } from './version.reducer';
import { IVersion } from 'app/shared/model/version.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVersionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVersionUpdateState {
  isNew: boolean;
  compId: string;
}

export class VersionUpdate extends React.Component<IVersionUpdateProps, IVersionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      compId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getComponentEntities();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { versionEntity } = this.props;
      const entity = {
        ...versionEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/version');
  };

  render() {
    const { versionEntity, componentEntities, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="myCmdbApp.version.home.createOrEditLabel">
              <Translate contentKey="myCmdbApp.version.home.createOrEditLabel">Create or edit a Version</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : versionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="version-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="versionStringLabel" for="versionString">
                    <Translate contentKey="myCmdbApp.version.versionString">Version String</Translate>
                  </Label>
                  <AvField
                    id="version-versionString"
                    type="text"
                    name="versionString"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="deployedByLabel" for="deployedBy">
                    <Translate contentKey="myCmdbApp.version.deployedBy">Deployed By</Translate>
                  </Label>
                  <AvField
                    id="version-deployedBy"
                    type="text"
                    name="deployedBy"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="gitCommitLabel" for="gitCommit">
                    <Translate contentKey="myCmdbApp.version.gitCommit">Git Commit</Translate>
                  </Label>
                  <AvField
                    id="version-gitCommit"
                    type="text"
                    name="gitCommit"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="gitCommitterLabel" for="gitCommitter">
                    <Translate contentKey="myCmdbApp.version.gitCommitter">Git Committer</Translate>
                  </Label>
                  <AvField
                    id="version-gitCommitter"
                    type="text"
                    name="gitCommitter"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="majorVersionLabel" for="majorVersion">
                    <Translate contentKey="myCmdbApp.version.majorVersion">Major Version</Translate>
                  </Label>
                  <AvField
                    id="version-majorVersion"
                    type="string"
                    className="form-control"
                    name="majorVersion"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="minorVersionLabel" for="minorVersion">
                    <Translate contentKey="myCmdbApp.version.minorVersion">Minor Version</Translate>
                  </Label>
                  <AvField
                    id="version-minorVersion"
                    type="string"
                    className="form-control"
                    name="minorVersion"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="hotfixNumberLabel" for="hotfixNumber">
                    <Translate contentKey="myCmdbApp.version.hotfixNumber">Hotfix Number</Translate>
                  </Label>
                  <AvField
                    id="version-hotfixNumber"
                    type="string"
                    className="form-control"
                    name="hotfixNumber"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="buildNumberLabel" for="buildNumber">
                    <Translate contentKey="myCmdbApp.version.buildNumber">Build Number</Translate>
                  </Label>
                  <AvField
                    id="version-buildNumber"
                    type="string"
                    className="form-control"
                    name="buildNumber"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="comp.comName">
                    <Translate contentKey="myCmdbApp.version.comp">Comp</Translate>
                  </Label>
                  <AvInput
                    id="version-comp"
                    type="select"
                    className="form-control"
                    name="comp.id"
                    value={isNew ? componentEntities[0] && componentEntities[0].id : versionEntity.comp.id}
                  >
                    {componentEntities
                      ? componentEntities.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.comName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/version" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  componentEntities: storeState.componentEntity.entities,
  versionEntity: storeState.version.entity,
  loading: storeState.version.loading,
  updating: storeState.version.updating,
  updateSuccess: storeState.version.updateSuccess
});

const mapDispatchToProps = {
  getComponentEntities,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VersionUpdate);
