import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IComponentEntity, defaultValue } from 'app/shared/model/component-entity.model';

export const ACTION_TYPES = {
  FETCH_COMPONENTENTITY_LIST: 'componentEntity/FETCH_COMPONENTENTITY_LIST',
  FETCH_COMPONENTENTITY: 'componentEntity/FETCH_COMPONENTENTITY',
  CREATE_COMPONENTENTITY: 'componentEntity/CREATE_COMPONENTENTITY',
  UPDATE_COMPONENTENTITY: 'componentEntity/UPDATE_COMPONENTENTITY',
  DELETE_COMPONENTENTITY: 'componentEntity/DELETE_COMPONENTENTITY',
  RESET: 'componentEntity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IComponentEntity>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ComponentEntityState = Readonly<typeof initialState>;

// Reducer

export default (state: ComponentEntityState = initialState, action): ComponentEntityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COMPONENTENTITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COMPONENTENTITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COMPONENTENTITY):
    case REQUEST(ACTION_TYPES.UPDATE_COMPONENTENTITY):
    case REQUEST(ACTION_TYPES.DELETE_COMPONENTENTITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_COMPONENTENTITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COMPONENTENTITY):
    case FAILURE(ACTION_TYPES.CREATE_COMPONENTENTITY):
    case FAILURE(ACTION_TYPES.UPDATE_COMPONENTENTITY):
    case FAILURE(ACTION_TYPES.DELETE_COMPONENTENTITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPONENTENTITY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMPONENTENTITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COMPONENTENTITY):
    case SUCCESS(ACTION_TYPES.UPDATE_COMPONENTENTITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COMPONENTENTITY):
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

const apiUrl = 'api/component-entities';

// Actions

export const getEntities: ICrudGetAllAction<IComponentEntity> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_COMPONENTENTITY_LIST,
  payload: axios.get<IComponentEntity>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IComponentEntity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COMPONENTENTITY,
    payload: axios.get<IComponentEntity>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IComponentEntity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COMPONENTENTITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IComponentEntity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COMPONENTENTITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IComponentEntity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COMPONENTENTITY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
