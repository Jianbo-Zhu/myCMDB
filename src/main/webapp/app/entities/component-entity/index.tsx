import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComponentEntity from './component-entity';
import ComponentEntityDetail from './component-entity-detail';
import ComponentEntityUpdate from './component-entity-update';
import ComponentEntityDeleteDialog from './component-entity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComponentEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComponentEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComponentEntityDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComponentEntity} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ComponentEntityDeleteDialog} />
  </>
);

export default Routes;
