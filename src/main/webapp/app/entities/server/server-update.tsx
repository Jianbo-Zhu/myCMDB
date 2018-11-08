import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDataCenter } from 'app/shared/model/data-center.model';
import { getEntities as getDataCenters } from 'app/entities/data-center/data-center.reducer';
import { IContactor } from 'app/shared/model/contactor.model';
import { getEntities as getContactors } from 'app/entities/contactor/contactor.reducer';
import { getEntity, updateEntity, createEntity, reset } from './server.reducer';
import { IServer } from 'app/shared/model/server.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServerUpdateState {
  isNew: boolean;
  dataCenterId: string;
  ownerId: string;
  vendorContactId: string;
}

export class ServerUpdate extends React.Component<IServerUpdateProps, IServerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      dataCenterId: '0',
      ownerId: '0',
      vendorContactId: '0',
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

    this.props.getDataCenters();
    this.props.getContactors();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { serverEntity } = this.props;
      const entity = {
        ...serverEntity,
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
    this.props.history.push('/entity/server');
  };

  render() {
    const { serverEntity, dataCenters, contactors, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="myCmdbApp.server.home.createOrEditLabel">
              <Translate contentKey="myCmdbApp.server.home.createOrEditLabel">Create or edit a Server</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serverEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="server-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="hostnameLabel" for="hostname">
                    <Translate contentKey="myCmdbApp.server.hostname">Hostname</Translate>
                  </Label>
                  <AvField
                    id="server-hostname"
                    type="text"
                    name="hostname"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="ipAddressLabel" for="ipAddress">
                    <Translate contentKey="myCmdbApp.server.ipAddress">Ip Address</Translate>
                  </Label>
                  <AvField
                    id="server-ipAddress"
                    type="text"
                    name="ipAddress"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="macAddressLabel" for="macAddress">
                    <Translate contentKey="myCmdbApp.server.macAddress">Mac Address</Translate>
                  </Label>
                  <AvField
                    id="server-macAddress"
                    type="text"
                    name="macAddress"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="positionLabel" for="position">
                    <Translate contentKey="myCmdbApp.server.position">Position</Translate>
                  </Label>
                  <AvField
                    id="server-position"
                    type="text"
                    name="position"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="brandLabel" for="brand">
                    <Translate contentKey="myCmdbApp.server.brand">Brand</Translate>
                  </Label>
                  <AvField
                    id="server-brand"
                    type="text"
                    name="brand"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="memSizeLabel" for="memSize">
                    <Translate contentKey="myCmdbApp.server.memSize">Mem Size</Translate>
                  </Label>
                  <AvField
                    id="server-memSize"
                    type="string"
                    className="form-control"
                    name="memSize"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="coreNoLabel" for="coreNo">
                    <Translate contentKey="myCmdbApp.server.coreNo">Core No</Translate>
                  </Label>
                  <AvField
                    id="server-coreNo"
                    type="string"
                    className="form-control"
                    name="coreNo"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="osVersionLabel" for="osVersion">
                    <Translate contentKey="myCmdbApp.server.osVersion">Os Version</Translate>
                  </Label>
                  <AvField
                    id="server-osVersion"
                    type="text"
                    name="osVersion"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="vendorLabel" for="vendor">
                    <Translate contentKey="myCmdbApp.server.vendor">Vendor</Translate>
                  </Label>
                  <AvField
                    id="server-vendor"
                    type="text"
                    name="vendor"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="purchaseDateLabel" for="purchaseDate">
                    <Translate contentKey="myCmdbApp.server.purchaseDate">Purchase Date</Translate>
                  </Label>
                  <AvField
                    id="server-purchaseDate"
                    type="date"
                    className="form-control"
                    name="purchaseDate"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="warrantyDateLabel" for="warrantyDate">
                    <Translate contentKey="myCmdbApp.server.warrantyDate">Warranty Date</Translate>
                  </Label>
                  <AvField
                    id="server-warrantyDate"
                    type="date"
                    className="form-control"
                    name="warrantyDate"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="dataCenter.dcName">
                    <Translate contentKey="myCmdbApp.server.dataCenter">Data Center</Translate>
                  </Label>
                  <AvInput
                    id="server-dataCenter"
                    type="select"
                    className="form-control"
                    name="dataCenter.id"
                    value={isNew ? dataCenters[0] && dataCenters[0].id : serverEntity.dataCenter.id}
                  >
                    {dataCenters
                      ? dataCenters.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.dcName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="owner.name">
                    <Translate contentKey="myCmdbApp.server.owner">Owner</Translate>
                  </Label>
                  <AvInput
                    id="server-owner"
                    type="select"
                    className="form-control"
                    name="owner.id"
                    value={isNew ? contactors[0] && contactors[0].id : serverEntity.owner.id}
                  >
                    {contactors
                      ? contactors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="vendorContact.name">
                    <Translate contentKey="myCmdbApp.server.vendorContact">Vendor Contact</Translate>
                  </Label>
                  <AvInput
                    id="server-vendorContact"
                    type="select"
                    className="form-control"
                    name="vendorContact.id"
                    value={isNew ? contactors[0] && contactors[0].id : serverEntity.vendorContact.id}
                  >
                    {contactors
                      ? contactors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/server" replace color="info">
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
  dataCenters: storeState.dataCenter.entities,
  contactors: storeState.contactor.entities,
  serverEntity: storeState.server.entity,
  loading: storeState.server.loading,
  updating: storeState.server.updating,
  updateSuccess: storeState.server.updateSuccess
});

const mapDispatchToProps = {
  getDataCenters,
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
)(ServerUpdate);
