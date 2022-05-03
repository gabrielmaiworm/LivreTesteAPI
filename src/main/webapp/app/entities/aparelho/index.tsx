import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Aparelho from './aparelho';
import AparelhoDetail from './aparelho-detail';
import AparelhoUpdate from './aparelho-update';
import AparelhoDeleteDialog from './aparelho-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AparelhoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AparelhoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AparelhoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Aparelho} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AparelhoDeleteDialog} />
  </>
);

export default Routes;
