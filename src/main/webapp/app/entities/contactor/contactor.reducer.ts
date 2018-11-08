import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContactor, defaultValue } from 'app/shared/model/contactor.model';

export const ACTION_TYPES = {
  FETCH_CONTACTOR_LIST: 'contactor/FETCH_CONTACTOR_LIST',
  FETCH_CONTACTOR: 'contactor/FETCH_CONTACTOR',
  CREATE_CONTACTOR: 'contactor/CREATE_CONTACTOR',
  UPDATE_CONTACTOR: 'contactor/UPDATE_CONTACTOR',
  DELETE_CONTACTOR: 'contactor/DELETE_CONTACTOR',
  RESET: 'contactor/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContactor>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ContactorState = Readonly<typeof initialState>;

// Reducer

export default (state: ContactorState = initialState, action): ContactorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONTACTOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTACTOR):
    case REQUEST(ACTION_TYPES.UPDATE_CONTACTOR):
    case REQUEST(ACTION_TYPES.DELETE_CONTACTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONTACTOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTOR):
    case FAILURE(ACTION_TYPES.CREATE_CONTACTOR):
    case FAILURE(ACTION_TYPES.UPDATE_CONTACTOR):
    case FAILURE(ACTION_TYPES.DELETE_CONTACTOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTACTOR):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTACTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTACTOR):
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

const apiUrl = 'api/contactors';

// Actions

export const getEntities: ICrudGetAllAction<IContactor> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTACTOR_LIST,
  payload: axios.get<IContactor>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IContactor> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTOR,
    payload: axios.get<IContactor>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IContactor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTACTOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContactor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTACTOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContactor> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTACTOR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
