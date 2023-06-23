import { People } from '@mui/icons-material'
import { Box, Container, Divider, Paper } from '@mui/material'
import {
  DataGrid,
  GridColDef,
  GridFilterItem,
  GridFilterModel,
  GridRenderCellParams,
  GridValueFormatterParams,
  GridValueGetterParams,
  useGridApiRef
} from '@mui/x-data-grid'
import { Link as LocationLink } from '@tanstack/react-location'
import { useQuery } from '@tanstack/react-query'
import { AxiosError } from 'axios'
import Loading from 'components/common/Loading'
import ApiErrorMessage from 'components/error/ApiErrorMessage'
import NotFound from 'components/error/NotFound'
import { formatDate, formatName, formatNumber } from 'utils/formatters'
import ApiClient from '../../services/ApiClient'
import Notification from '../../types/Notification'
import PageHeader from '../shell/PageHeader'
import DataGridToolbar from './DataGridToolBar'
import NotificationState from './NotificationState'
import React, { useCallback, useState } from 'react'
import dayjs, { Dayjs } from 'dayjs'
import locale from 'dayjs/locale/uk'
import { LocalizationProvider, DesktopDatePicker } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'

const columns: GridColDef[] = [
  {
    field: 'state',
    headerName: 'Status',
    minWidth: 150,
    align: 'center',
    headerAlign: 'center',
    renderCell: (params: GridRenderCellParams) => {
      return <NotificationState value={params.value || ''} />
    }
  },
  {
    field: 'created',
    headerName: 'Date',
    type: 'date',
    minWidth: 150,

    align: 'center',
    headerAlign: 'center',
    valueFormatter: (params: GridValueFormatterParams<Date>) =>
      formatDate(params.value),
    filterable: false
  },
  // {
  //   field: '',
  //   headerName: 'Notification Type',
  //   minWidth: 150
  // },
  {
    field: 'patient_id',
    headerName: 'Interaction ID',
    type: 'number',
    minWidth: 150,
    align: 'center',
    headerAlign: 'center',
    filterable: false
  },
  {
    field: 'golden_id',
    headerName: 'Golden ID',
    type: 'number',
    minWidth: 150,
    align: 'center',
    headerAlign: 'center',
    filterable: false
  },
  {
    field: 'score',
    headerName: 'Score',
    type: 'number',
    minWidth: 150,
    align: 'center',
    headerAlign: 'center',
    valueGetter: (params: GridValueGetterParams) => params.row.score,
    valueFormatter: params => formatNumber(params.value),
    filterable: false
  },
  {
    field: 'type',
    headerName: 'Notification Reason',
    minWidth: 150,
    align: 'center',
    filterable: false
  },
  {
    field: 'names',
    headerName: 'Patient',
    minWidth: 150,
    valueFormatter: (params: GridValueFormatterParams<string>) =>
      formatName(params.value),
    filterable: false
  },
  {
    field: 'actions',
    headerName: 'Actions',
    maxWidth: 150,
    flex: 1,
    align: 'center',
    headerAlign: 'center',
    sortable: false,
    filterable: false,
    valueGetter: (params: GridValueGetterParams) => ({
      id: params.row.id,
      patient: params.row.patient
    }),
    renderCell: (params: GridRenderCellParams<Notification>) => {
      const { patient_id, candidates, score, id, golden_id, status } =
        params.row
      return (
        <LocationLink
          to={`/notifications/match-details`}
          search={{
            payload: {
              notificationId: id,
              patient_id,
              golden_id,
              score,
              candidates
            }
          }}
          style={{ textDecoration: 'none' }}
        >
          {status !== 'Actioned' ? 'VIEW' : null}
        </LocationLink>
      )
    }
  }
]

const NotificationWorklist = () => {
  const selectedDate = dayjs().locale({
    ...locale
  })
  const [date, setDate] = React.useState(selectedDate)
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 25
  })
  const [filterModel, setFilterModel] = useState<GridFilterModel>({
    items: [{ field: 'state', value: 'New', operator: 'contains' }]
  })
  const { data, error, isLoading, isFetching } = useQuery<
    Notification[],
    AxiosError
  >({
    queryKey: [
      'notifications',
      date.format('YYYY-MM-DD'),
      paginationModel.page,
      paginationModel.pageSize,
      filterModel
    ],
    queryFn: () =>
      ApiClient.getMatches(
        paginationModel.pageSize,
        paginationModel.page * paginationModel.pageSize,
        date.format('YYYY-MM-DD'),
        filterModel.items[0].value ? filterModel.items[0].value : ''
      ),
    refetchOnWindowFocus: false
  })

  const onFilterChange = useCallback((filterModel: GridFilterModel) => {
    setFilterModel({ ...filterModel })
  }, [])

  if (isLoading || isFetching) {
    return <Loading />
  }

  if (error) {
    return <ApiErrorMessage error={error} />
  }

  if (!data) {
    return <NotFound />
  }

  const changeSelectedDate = (date: Dayjs | null) => {
    if (date) {
      setDate(date)
    }
  }
  return (
    <Container maxWidth={false}>
      <PageHeader
        title={'Notification Worklist'}
        breadcrumbs={[
          {
            link: '/review-matches/',
            title: 'Notifications',
            icon: <People />
          }
        ]}
      />
      <Divider />
      <Paper
        sx={{
          p: 1,
          mt: 4,
          display: 'flex',
          flexDirection: 'column',
          gap: '15px'
        }}
      >
        <Box p={1}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DesktopDatePicker
              value={date}
              format="YYYY/MM/DD"
              onChange={value => changeSelectedDate(value)}
              slotProps={{
                textField: {
                  variant: 'outlined',
                  label: 'We are looking to name this'
                }
              }}
            />
          </LocalizationProvider>
        </Box>
        <DataGrid
          sx={{ height: '500px' }}
          columns={columns}
          rows={data as Notification[]}
          pageSizeOptions={[10, 25, 50]}
          paginationModel={paginationModel}
          onPaginationModelChange={model => setPaginationModel(model)}
          paginationMode="server"
          rowCount={1000000}
          filterMode="server"
          filterModel={filterModel}
          onFilterModelChange={model => onFilterChange(model)}
        />
      </Paper>
    </Container>
  )
}

export default NotificationWorklist
