package com.algderno.controllers.helper.data;

public class DataLineChart {
/*
	private SimpleLogger logger;

	private String nameChart;

	private List<Exercise> listExercises;

	private XYChart.Series<String, Number> series;

	private List<DataPopup> datasPopup;

	private HelperChartController helper;

	public DataLineChart(HelperChartController helper, SimpleLogger logger, String nameChart, List<Exercise> listExercises) {

		this.helper = helper;

		this.logger = logger;

		this.nameChart = nameChart;

		this.listExercises = listExercises;

		this.series = new XYChart.Series<String, Number>();

	}

	public void addMouseListeners() throws Exception {

		XYChart.Data<String, Number> data = null;

		for (int i = 0; series.getData().size() > i; i++) {

			data = (XYChart.Data<String, Number>) series.getData().get(i);

			addListenerData(data, datasPopup.get(i));

		}

		Collections.sort(series.getData(), sortData());

	}

	public void addListenerData(XYChart.Data<String, Number> data, DataPopup dataPopup) throws Exception {

		Node node = data.getNode();

		if (node == null)
			throw new NullPointerException("Node of XYChart.Data is null. Add in one chart first!");

		// Add action popup in action mouse
		node.setOnMouseEntered((e) -> {
			helper.showPopup(dataPopup, nameChart, e.getScreenX(), (e.getScreenY()-90));
		});

		node.setOnMouseExited((e) -> helper.hidePopup() );

	}

	public XYChart.Series<String, Number> createSeries() {

		datasPopup = new ArrayList<>();

		ObservableList<XYChart.Data<String, Number>> dataSeries = FXCollections.observableArrayList(); 

		series.setName(nameChart);

		// Extract data of mapExercises
		for (int i = 0; listExercises.size() > i; i++) // Exercises

			for (int j = 0; listExercises.get(i).getMapData().size() > j; j++) { // Questions

				// Init Question
				Question question = listExercises.get(i).getMapData().get(j+"");

				addCurrentData("ExerciseName", question, datasPopup, dataSeries);

			}

		series.setData(dataSeries);

		return series;

	}

	public int addCurrentData(String exerciseName, Question question, 
					List<DataPopup> datasPoput, 
					ObservableList<XYChart.Data<String, Number>> dataSeries) {

		datasPopup.add(
				new DataPopup(
					question.getName(), 
					question.getLastRuntime(), 
					exerciseName
				)
		);

		dataSeries.add(
			new XYChart.Data<String, Number>(
				question.getName() + " - " + exerciseName, 
				question.getLastRuntime()
			)
		);

		return dataSeries.size()-1; // return index

	}

	private Comparator<XYChart.Data<String, Number>> sortData() {

		return new Comparator<XYChart.Data<String, Number>>() {

			@Override
			public int compare(XYChart.Data d1, XYChart.Data d2) {

				Number yValue1 = (Number) d1.getYValue();
				Number yValue2 = (Number) d2.getYValue();
System.out.println("d1 = "+ yValue1 + " d2 = " + yValue1 + " - " + new BigDecimal(yValue1.toString()));
				return new BigDecimal(yValue1.toString())
						.compareTo(
							new BigDecimal(yValue2.toString())
						);

			}

		};

	}

	public List<DataPopup> getDatasPopup() {
		return datasPopup;
	}
*/
}

