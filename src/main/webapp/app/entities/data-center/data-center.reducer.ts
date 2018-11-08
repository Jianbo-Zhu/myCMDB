import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDataCenter, defaultValue } from 'app/shared/model/data-center.model';

export const ACTION_TYPES = {
  FETCH_DATACENTER_LIST: 'dataCenter/FETCH_DATACENTER_LIST',
  FETCH_DATACENTER: 'dataCenter/FETCH_DATACENTER',
  CREATE_DATACENTER: 'dataCenter/CREATE_DATACENTER',
  UPDATE_DATACENTER: 'dataCenter/UPDATE_DATACENTER',
  DELETE_DATACENTER: 'dataCenter/DELETE_DATACENTER',
  RESET: 'dataCenter/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDataCenter>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type DataCenterState = Readonly<typeof initialState>;

// Reducer

export default (state: DataCenterState = initialState, action): DataCenterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DATACENTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DATACENTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DATACENTER):
    case REQUEST(ACTION_TYPES.UPDATE_DATACENTER):
    case REQUEST(ACTION_TYPES.DELETE_DATACENTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DATACENTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DATACENTER):
    case FAILURE(ACTION_TYPES.CREATE_DATACENTER):
    case FAILURE(ACTION_TYPES.UPDATE_DATACENTER):
    case FAILURE(ACTION_TYPES.DELETE_DATACENTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATACENTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATACENTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DATACENTER):
    case SUCCESS(ACTION_TYPES.UPDATE_DATACENTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DATACENTER):
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

const apiUrl = 'api/data-centers';

// Actions

export const getEntities: ICrudGetAllAction<IDataCenter> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DATACENTER_LIST,
  payload: axios.get<IDataCenter>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IDataCenter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DATACENTER,
    payload: axios.get<IDataCenter>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDataCenter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DATACENTER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDataCenter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DATACENTER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDataCenter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DATACENTER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
