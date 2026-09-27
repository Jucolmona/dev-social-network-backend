DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'usuarios') THEN
        ALTER TABLE usuarios
            ADD COLUMN IF NOT EXISTS nombre VARCHAR(255),
            ADD COLUMN IF NOT EXISTS apellido VARCHAR(255),
            ADD COLUMN IF NOT EXISTS fecha_creacion DATE;

        UPDATE usuarios
        SET nombre = 'Usuario'
        WHERE nombre IS NULL;

        UPDATE usuarios
        SET apellido = 'Sin apellido'
        WHERE apellido IS NULL;

        UPDATE usuarios
        SET fecha_creacion = CURRENT_DATE
        WHERE fecha_creacion IS NULL;

        ALTER TABLE usuarios
            ALTER COLUMN nombre SET NOT NULL,
            ALTER COLUMN apellido SET NOT NULL,
            ALTER COLUMN fecha_creacion SET NOT NULL;
    END IF;
END $$;
