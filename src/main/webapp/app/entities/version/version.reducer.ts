import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVersion, defaultValue } from 'app/shared/model/version.model';

export const ACTION_TYPES = {
  FETCH_VERSION_LIST: 'version/FETCH_VERSION_LIST',
  FETCH_VERSION: 'version/FETCH_VERSION',
  CREATE_VERSION: 'version/CREATE_VERSION',
  UPDATE_VERSION: 'version/UPDATE_VERSION',
  DELETE_VERSION: 'version/DELETE_VERSION',
  RESET: 'version/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVersion>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type VersionState = Readonly<typeof initialState>;

// Reducer

export default (state: VersionState = initialState, action): VersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VERSION):
    case REQUEST(ACTION_TYPES.UPDATE_VERSION):
    case REQUEST(ACTION_TYPES.DELETE_VERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VERSION):
    case FAILURE(ACTION_TYPES.CREATE_VERSION):
    case FAILURE(ACTION_TYPES.UPDATE_VERSION):
    case FAILURE(ACTION_TYPES.DELETE_VERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VERSION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_VERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_VERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/versions';

// Actions

export const getEntities: ICrudGetAllAction<IVersion> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_VERSION_LIST,
  payload: axios.get<IVersion>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VERSION,
    payload: axios.get<IVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
