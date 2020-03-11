"""link db

Revision ID: ae31f81e2a0e
Revises: 
Create Date: 2020-03-04 21:27:50.465963

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'ae31f81e2a0e'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_index(op.f('ix_MainData_errorRate'), 'MainData', ['errorRate'], unique=False)
    op.create_index(op.f('ix_MainData_postCode'), 'MainData', ['postCode'], unique=False)
    op.create_index(op.f('ix_MainData_supportCode'), 'MainData', ['supportCode'], unique=False)
    op.create_index(op.f('ix_MainData_weeklyCalls'), 'MainData', ['weeklyCalls'], unique=False)
    op.create_index(op.f('ix_MainData_weeklySteps'), 'MainData', ['weeklySteps'], unique=False)
    op.create_index(op.f('ix_MainData_wellBeingScore'), 'MainData', ['wellBeingScore'], unique=False)
    op.drop_index('ix_CoreData_errorRate', table_name='MainData')
    op.create_index(op.f('ix_UpdateAggregator_errorRate'), 'UpdateAggregator', ['errorRate'], unique=False)
    op.create_index(op.f('ix_UpdateAggregator_postCode'), 'UpdateAggregator', ['postCode'], unique=False)
    op.create_index(op.f('ix_UpdateAggregator_supportCode'), 'UpdateAggregator', ['supportCode'], unique=False)
    op.create_index(op.f('ix_UpdateAggregator_weeklyCalls'), 'UpdateAggregator', ['weeklyCalls'], unique=False)
    op.create_index(op.f('ix_UpdateAggregator_weeklySteps'), 'UpdateAggregator', ['weeklySteps'], unique=False)
    op.create_index(op.f('ix_UpdateAggregator_wellBeingScore'), 'UpdateAggregator', ['wellBeingScore'], unique=False)
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_index(op.f('ix_UpdateAggregator_wellBeingScore'), table_name='UpdateAggregator')
    op.drop_index(op.f('ix_UpdateAggregator_weeklySteps'), table_name='UpdateAggregator')
    op.drop_index(op.f('ix_UpdateAggregator_weeklyCalls'), table_name='UpdateAggregator')
    op.drop_index(op.f('ix_UpdateAggregator_supportCode'), table_name='UpdateAggregator')
    op.drop_index(op.f('ix_UpdateAggregator_postCode'), table_name='UpdateAggregator')
    op.drop_index(op.f('ix_UpdateAggregator_errorRate'), table_name='UpdateAggregator')
    op.create_index('ix_CoreData_errorRate', 'MainData', ['errorRate'], unique=False)
    op.drop_index(op.f('ix_MainData_wellBeingScore'), table_name='MainData')
    op.drop_index(op.f('ix_MainData_weeklySteps'), table_name='MainData')
    op.drop_index(op.f('ix_MainData_weeklyCalls'), table_name='MainData')
    op.drop_index(op.f('ix_MainData_supportCode'), table_name='MainData')
    op.drop_index(op.f('ix_MainData_postCode'), table_name='MainData')
    op.drop_index(op.f('ix_MainData_errorRate'), table_name='MainData')
    # ### end Alembic commands ###