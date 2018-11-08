import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Contactor from './contactor';
import ContactorDetail from './contactor-detail';
import ContactorUpdate from './contactor-update';
import ContactorDeleteDialog from './contactor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContactorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContactorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContactorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Contactor} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ContactorDeleteDialog} />
  </>
);

export default Routes;
