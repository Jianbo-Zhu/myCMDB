import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './component-entity.reducer';
import { IComponentEntity } from 'app/shared/model/component-entity.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IComponentEntityDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ComponentEntityDetail extends React.Component<IComponentEntityDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { componentEntityEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="myCmdbApp.componentEntity.detail.title">ComponentEntity</Translate> [<b>{componentEntityEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="comName">
                <Translate contentKey="myCmdbApp.componentEntity.comName">Com Name</Translate>
              </span>
            </dt>
            <dd>{componentEntityEntity.comName}</dd>
            <dt>
              <span id="comType">
                <Translate contentKey="myCmdbApp.componentEntity.comType">Com Type</Translate>
              </span>
            </dt>
            <dd>{componentEntityEntity.comType}</dd>
            <dt>
              <Translate contentKey="myCmdbApp.componentEntity.app">App</Translate>
            </dt>
            <dd>{componentEntityEntity.app ? componentEntityEntity.app.appName : ''}</dd>
            <dt>
              <Translate contentKey="myCmdbApp.componentEntity.server">Server</Translate>
            </dt>
            <dd>{componentEntityEntity.server ? componentEntityEntity.server.hostname : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/component-entity" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/component-entity/${componentEntityEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ componentEntity }: IRootState) => ({
  componentEntityEntity: componentEntity.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ComponentEntityDetail);
