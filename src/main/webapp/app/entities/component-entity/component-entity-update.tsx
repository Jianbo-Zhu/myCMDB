import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IApplication } from 'app/shared/model/application.model';
import { getEntities as getApplications } from 'app/entities/application/application.reducer';
import { IServer } from 'app/shared/model/server.model';
import { getEntities as getServers } from 'app/entities/server/server.reducer';
import { getEntity, updateEntity, createEntity, reset } from './component-entity.reducer';
import { IComponentEntity } from 'app/shared/model/component-entity.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IComponentEntityUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IComponentEntityUpdateState {
  isNew: boolean;
  appId: string;
  serverId: string;
}

export class ComponentEntityUpdate extends React.Component<IComponentEntityUpdateProps, IComponentEntityUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      appId: '0',
      serverId: '0',
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

    this.props.getApplications();
    this.props.getServers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { componentEntityEntity } = this.props;
      const entity = {
        ...componentEntityEntity,
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
    this.props.history.push('/entity/component-entity');
  };

  render() {
    const { componentEntityEntity, applications, servers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="myCmdbApp.componentEntity.home.createOrEditLabel">
              <Translate contentKey="myCmdbApp.componentEntity.home.createOrEditLabel">Create or edit a ComponentEntity</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : componentEntityEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="component-entity-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="comNameLabel" for="comName">
                    <Translate contentKey="myCmdbApp.componentEntity.comName">Com Name</Translate>
                  </Label>
                  <AvField
                    id="component-entity-comName"
                    type="text"
                    name="comName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="comTypeLabel">
                    <Translate contentKey="myCmdbApp.componentEntity.comType">Com Type</Translate>
                  </Label>
                  <AvInput
                    id="component-entity-comType"
                    type="select"
                    className="form-control"
                    name="comType"
                    value={(!isNew && componentEntityEntity.comType) || 'MODULE'}
                  >
                    <option value="MODULE">
                      <Translate contentKey="myCmdbApp.ComponentType.MODULE" />
                    </option>
                    <option value="MIDDLEWARE">
                      <Translate contentKey="myCmdbApp.ComponentType.MIDDLEWARE" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="app.appName">
                    <Translate contentKey="myCmdbApp.componentEntity.app">App</Translate>
                  </Label>
                  <AvInput id="component-entity-app" type="select" className="form-control" name="app.id">
                    <option value="" key="0" />
                    {applications
                      ? applications.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.appName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="server.hostname">
                    <Translate contentKey="myCmdbApp.componentEntity.server">Server</Translate>
                  </Label>
                  <AvInput
                    id="component-entity-server"
                    type="select"
                    className="form-control"
                    name="server.id"
                    value={isNew ? servers[0] && servers[0].id : componentEntityEntity.server.id}
                  >
                    {servers
                      ? servers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.hostname}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/component-entity" replace color="info">
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
  applications: storeState.application.entities,
  servers: storeState.server.entities,
  componentEntityEntity: storeState.componentEntity.entity,
  loading: storeState.componentEntity.loading,
  updating: storeState.componentEntity.updating,
  updateSuccess: storeState.componentEntity.updateSuccess
});

const mapDispatchToProps = {
  getApplications,
  getServers,
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
)(ComponentEntityUpdate);
