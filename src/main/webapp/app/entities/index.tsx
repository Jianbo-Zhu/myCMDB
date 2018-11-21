import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Contactor from './contactor';
import DataCenter from './data-center';
import Application from './application';
import ComponentEntity from './component-entity';
import Server from './server';
import Version from './version';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/contactor`} component={Contactor} />
      <ErrorBoundaryRoute path={`${match.url}/data-center`} component={DataCenter} />
      <ErrorBoundaryRoute path={`${match.url}/application`} component={Application} />
      <ErrorBoundaryRoute path={`${match.url}/component-entity`} component={ComponentEntity} />
      <ErrorBoundaryRoute path={`${match.url}/server`} component={Server} />
      <ErrorBoundaryRoute path={`${match.url}/version`} component={Version} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
