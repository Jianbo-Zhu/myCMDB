import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IContactor } from 'app/shared/model/contactor.model';
import { getEntities as getContactors } from 'app/entities/contactor/contactor.reducer';
import { getEntity, updateEntity, createEntity, reset } from './data-center.reducer';
import { IDataCenter } from 'app/shared/model/data-center.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDataCenterUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDataCenterUpdateState {
  isNew: boolean;
  contactorId: string;
}

export class DataCenterUpdate extends React.Component<IDataCenterUpdateProps, IDataCenterUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      contactorId: '0',
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

    this.props.getContactors();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { dataCenterEntity } = this.props;
      const entity = {
        ...dataCenterEntity,
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
    this.props.history.push('/entity/data-center');
  };

  render() {
    const { dataCenterEntity, contactors, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="myCmdbApp.dataCenter.home.createOrEditLabel">
              <Translate contentKey="myCmdbApp.dataCenter.home.createOrEditLabel">Create or edit a DataCenter</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : dataCenterEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="data-center-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dcNameLabel" for="dcName">
                    <Translate contentKey="myCmdbApp.dataCenter.dcName">Dc Name</Translate>
                  </Label>
                  <AvField
                    id="data-center-dcName"
                    type="text"
                    name="dcName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="address">
                    <Translate contentKey="myCmdbApp.dataCenter.address">Address</Translate>
                  </Label>
                  <AvField
                    id="data-center-address"
                    type="text"
                    name="address"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="contactor.name">
                    <Translate contentKey="myCmdbApp.dataCenter.contactor">Contactor</Translate>
                  </Label>
                  <AvInput id="data-center-contactor" type="select" className="form-control" name="contactor.id">
                    <option value="" key="0" />
                    {contactors
                      ? contactors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/data-center" replace color="info">
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
  contactors: storeState.contactor.entities,
  dataCenterEntity: storeState.dataCenter.entity,
  loading: storeState.dataCenter.loading,
  updating: storeState.dataCenter.updating,
  updateSuccess: storeState.dataCenter.updateSuccess
});

const mapDispatchToProps = {
  getContactors,
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
)(DataCenterUpdate);
