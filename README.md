# dev-social-network-backend
Proyecto de fabrica escuela para el curso de Arquitectura de software y base de datos - UdeA 2026 - 2, para el programa de Ingenieria de Sistemas virtual

## Migraciones manuales requeridas

Antes de desplegar cambios que agregan columnas obligatorias en `usuarios`, ejecutar:

`dev-social-network/src/main/resources/db/migration/V1__usuarios_required_columns_backfill.sql`
