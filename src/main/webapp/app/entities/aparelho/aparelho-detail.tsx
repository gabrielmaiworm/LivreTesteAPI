import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aparelho.reducer';

export const AparelhoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aparelhoEntity = useAppSelector(state => state.aparelho.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aparelhoDetailsHeading">Aparelho</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{aparelhoEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{aparelhoEntity.nome}</dd>
          <dt>
            <span id="numeroSerie">Numero Serie</span>
          </dt>
          <dd>{aparelhoEntity.numeroSerie}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{aparelhoEntity.status}</dd>
          <dt>
            <span id="carga">Carga</span>
          </dt>
          <dd>{aparelhoEntity.carga}</dd>
        </dl>
        <Button tag={Link} to="/aparelho" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aparelho/${aparelhoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AparelhoDetail;
