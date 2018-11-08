import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DataCenter from './data-center';
import DataCenterDetail from './data-center-detail';
import DataCenterUpdate from './data-center-update';
import DataCenterDeleteDialog from './data-center-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DataCenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DataCenterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DataCenterDetail} />
      <ErrorBoundaryRoute path={match.url} component={DataCenter} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DataCenterDeleteDialog} />
  </>
);

export default Routes;
