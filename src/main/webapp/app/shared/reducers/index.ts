import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import contactor, {
  ContactorState
} from 'app/entities/contactor/contactor.reducer';
// prettier-ignore
import dataCenter, {
  DataCenterState
} from 'app/entities/data-center/data-center.reducer';
// prettier-ignore
import application, {
  ApplicationState
} from 'app/entities/application/application.reducer';
// prettier-ignore
import componentEntity, {
  ComponentEntityState
} from 'app/entities/component-entity/component-entity.reducer';
// prettier-ignore
import server, {
  ServerState
} from 'app/entities/server/server.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly contactor: ContactorState;
  readonly dataCenter: DataCenterState;
  readonly application: ApplicationState;
  readonly componentEntity: ComponentEntityState;
  readonly server: ServerState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  contactor,
  dataCenter,
  application,
  componentEntity,
  server,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
