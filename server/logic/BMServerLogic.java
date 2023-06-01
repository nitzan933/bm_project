package server.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.db_logic.BusinessClientDBController;
import server.db_logic.BusinessDBController;
import server.db_logic.ClientDBController;
import server.db_logic.DBController;
import server.db_logic.DishesDBController;
import server.db_logic.HRDBController;
import server.db_logic.OpenReoprtDBController;
import server.db_logic.OrderDBController;
import server.db_logic.RestaurantDBController;
import server.db_logic.SupplierDBController;
import server.db_logic.UserDBController;
import server.exceptions.BMServerException;
import server.gui.ServerMainWindowController;
import utility.entity.Business;
import utility.entity.BusinessClientData;
import utility.entity.Client;
import utility.entity.ClientRefundsData;
import utility.entity.Dish;
import utility.entity.Order;
import utility.entity.Restaurant;
import utility.entity.Supplier;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.ErrorType;
import utility.enums.RequestType;
import utility.message_parsers.MessageParser;
import utility.message_parsers.MessageParserBranchManager;
import utility.message_parsers.MessageParserBusinessClientData;
import utility.message_parsers.MessageParserClientRefundData;
import utility.message_parsers.MessageParserError;
import utility.message_parsers.MessageParserHR;
import utility.message_parsers.MessageParserOrder;
import utility.message_parsers.MessageParserTextString;
import utility.message_parsers.MessageParserUser;
import utility.message_parsers.MessegeParserDishes;
import utility.message_parsers.MessegeParserRestaurants;


public class BMServerLogic extends AbstractServer {

	ServerMainWindowController guiController;

	DBController dbController;
	OrderDBController orderDBController;
	UserDBController userDBController;
	BusinessDBController businessDBConteroller;
	ClientDBController clientDBController;
	SupplierDBController supplierDBController;
	RestaurantDBController restaurantDBController;
	HRDBController hrDBController;
	DishesDBController dishesDBController;
	BusinessClientDBController budDBController;
	OpenReoprtDBController openReoprtDBController;
//	 ReportsDBController reportDBCOntroller;
	
	
	boolean flagFileIncoming = false;
	ConnectionToClient fileSenderClient = null;

	public BMServerLogic(int port, String dbName, String dbUser, String dbPassword) throws Exception {
		super(port);

		// Initializing controllers used on server side:
		this.dbController = DBController.getDBControllerInstanceFor(dbName, dbUser, dbPassword);
		dbController.connectToDBServer();
		if (!dbController.isConnected()) {
			// error connecting to db, no reason to go on. TODO: change error handling
			System.out.println("Error Connecting to DB, can't init Server logic");
			throw new Exception("Can't Connect To DB, Server not started");
		}
		this.orderDBController = new OrderDBController(dbController);
		this.userDBController = new UserDBController(dbController);
		this.clientDBController = new ClientDBController(dbController);
		this.supplierDBController = new SupplierDBController(dbController);
		this.restaurantDBController = new RestaurantDBController(dbController);
		this.hrDBController = new HRDBController(dbController);
		this.dishesDBController = new DishesDBController(dbController);
		this.businessDBConteroller = new BusinessDBController(dbController);
		this.openReoprtDBController = new OpenReoprtDBController(dbController);
		this.budDBController = new BusinessClientDBController(dbController);
		userDBController.setAllUsersToLoggedOut();
	    Thread thread = new Thread(new Scheduler(dbController));
	    thread.start();
	}

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
//		//TEST:----------------
//			if(fileIncoming == true && client == clientSendingFile) {
//				//take care of file
//				//filenIncoming = false;
//				//return;
//			}
//		//-*------------------
		
		// we are assuming message is ArrayList<String>
		RequestType actionRequired = MessageParser.parseMessage_RequestType(msg);
		if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA) {
			// if client requests data - we call the request function to handle
			handleGetRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_NEW_TO_DB
				|| actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB) {
			// if client wants to write data - we call the write function to handle
			handleWriteRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CONNECTION_STATUS) {
			handleConnectionRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_DEBUG_MESSAGE) {
			// this is a debug case
			handleDebugRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_LOGIN_REQUEST
				|| actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_LOGOUT_REQUEST) {
			handleLoginRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_W4C_REQUEST) {
			handleW4CRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_CLIENT) {
			handleRegisterClientRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_SUPPLIER) {
			handleRegisterSupplierRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST) {
			handleSearchRequest(actionRequired, msg, client);
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_INCOMING_FILE) {
			handleFileIncomingRequest(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST) {
			handleGetRestaurantsByCategoryRequest(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_MENU_REQUEST) {
			handleGetMenuRequest(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_DATA 
				|| actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_BUDGED_UPDATE) {
			handleGetBusinessClientData(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_CLIENT_REFUNDS_DATA 
				|| actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_REFUND_AMOUNT_AFTER_REFUNDS_USED) {
			handleCRFRequest(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_BUSINESSES_NAMES) {
			handleGetDataOfBusiness(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_APPROVE_BUSINESS) {
			handleApproveBusiness(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_CLIENT_INFO) {
			handleGetDataOfClient(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CHANGE_PERMISSION) {
			handleChangePermission(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT) {
			handleOpenReportRequest(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_SEND_REPORT_TO_CEO) {
			handleSendReportToCEO(actionRequired, msg, client);
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_APRROVE_BUSINESS) {
            handleApproveBusinessRequest(actionRequired,msg,client);
        }
        else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CHECK_APRROVE_BUSINESS) {
            handleCheckApproveBusinessRequest(actionRequired,msg,client);
        }
        else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS) {
            handleGetRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST) {
            handleGetResBySupplierRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST) {
            handleAddDishToMenuRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST) {
            handleUpdateDishInMenuRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST) {
            handleDeleteDishFromMenuRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST) {
            handleGetOrdersFromSupplierRequest(actionRequired, msg, client);
        } else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER) {
            handleCancelOrder(actionRequired, msg, client);
        } else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER) {
            handleOrdersStatus(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CEO_QUARTERLY_REPORT) {
        	handleQuarterlyReportRequest(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CEO_GET_BRANCHES_REPORTS) {
        	handleGetReportFromCEO(actionRequired, msg, client);
        }else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT_FROM_BRANCH) {
        	handleOpenReportFromBranch(actionRequired, msg, client);
        }else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_SUPPLIER_INCOME_REPORT) {
        	handleOpenIncomeReportForSupplier(actionRequired, msg, client);
        }
		serverPrintToGuiLog("Message From Client Handled, action: " + actionRequired.toString(), true);
	}

	


	


	public void sendMessageToGivenClient(Object msg, ConnectionToClient client) {
		try {
			client.sendToClient(msg);
		} catch (IOException e) {
			// TODO: HANDLE ERROR
			e.printStackTrace();
		}
	}

	protected void serverStarted() {
		serverPrintToGuiLog("Server listening for connections on port " + getPort(), true);
	}

	protected void serverStopped() {
		serverPrintToGuiLog("Server has stopped listening for connections.", true);
	}

	protected void clientConnected(ConnectionToClient client) {
		serverPrintToGuiNumberOfClients(getNumberOfClients());
		serverPrintToGuiLog("Client Connected: " + client.toString(), true);
	}

//	synchronized protected void clientDisconnected(ConnectionToClient client) {
////		System.out.println("Client Disconnected:" + client.toString());
//		serverPrintToGuiLog("Client Disconnected:" + client.toString(), true);
//		int numOfClients = getNumberOfClients() - 1;
//		serverPrintToGuiNumberOfClients(numOfClients);
//	}

	protected void clientDisconnectedNonAbstract(ConnectionToClient client) {
		serverPrintToGuiLog("Client Disconnected: " + client.toString(), true);
		int numOfClients = getNumberOfClients() - 1;
		serverPrintToGuiNumberOfClients(numOfClients);
	}

	// ---------------------- HELPER FUNCTIONS --------------------------

	private void handleWriteRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		DataType messageDataType = MessageParser.parseMessage_DataType(msg);
		switch (messageDataType) {
		case ORDER:
			System.out.println("handleWriteRequest, ORDER");
			Order orderData = MessageParserOrder.handleMessageExtractDataType_Order(msg);
			orderDBController.writeOrderDataToDB(orderData);
			break;
		case HR_MANAGER:
			boolean response = HRDBController.updateUsersInDB((ArrayList<String>)msg);
			if(response == false) {
				sendMessageToGivenClient(ErrorType.COULD_NOT_UPDATE_BUSINESS_CLIENT,client);
			}
			break;
		case UNKNOWN:
			// TODO: HANDLE ERROR - UNKNOWN DATA TYPE?
		default:
			// TODO: HANDE ERROR - HOW DID WE GET HERE?
		}
		
	}

	private void handleGetRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		DataType messageDataType = MessageParser.parseMessage_DataType(msg);
		switch (messageDataType) {
		case ORDER:
//				String requestedOrder = MessageParser.parseMessageDataType_Order_GetRequestOrderID(msg);
//				Order order = orderDBController.getOrderDataFromDB(requestedOrder);
//				Object response;
//				if(order == null) {
//					//TOOD:Add error handling
//					String error = "Error: Failed to find requestedOrder in DB";
//					response = MessageParser.createMessageToClientDataType_Error(error);
//				} else {
//					response = MessageParser.createMessageToClientDataType_Order(order);
//				}
//				sendMessageToGivenClient(response,client);
			break;
		case HR_MANAGER:
            if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS) {
                ArrayList<String> gotFromClient = MessageParserHR.handleMessageFromClient_HRGetData(msg);
                Object response = HRDBController.getApprovedUsers(Integer.parseInt(gotFromClient.get(2)));
                sendMessageToGivenClient(response,client);
            }
            else {
                ArrayList<String> gotFromClient = MessageParserHR.handleMessageFromClient_HRGetData(msg);
                Object response = HRDBController.getUsersToApproveFromDB(Integer.parseInt(gotFromClient.get(2)));
                sendMessageToGivenClient(response,client);
            }
			break; 
		case UNKNOWN:
			// TODO: HANDLE ERROR - UNKNOWN DATA TYPE?
		default:
			// TODO: HANDE ERROR - HOW DID WE GET HERE?
		}
	}

	private void handleConnectionRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		DataType messageDataType = MessageParser.parseMessage_DataType(msg);
		switch (messageDataType) {
		case SINGLE_TEXT_STRING:
			String message = MessageParserTextString.handleMessageExtractDataType_SingleTextString(msg);
			if (message.equals("disconnected")) {
				try {
					clientDisconnectedNonAbstract(client);
					client.close();
				} catch (IOException e) {
					// TODO: add error handling
					e.printStackTrace();
				}
			}
			break;
		default:
			// TODO: HANDE ERROR - HOW DID WE GET HERE?
		}
	}

	private void handleLoginRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		User user = MessageParserUser.handleMessageExtractDataType_User(msg);
		if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_LOGIN_REQUEST) {
			Object response;
			try {
				User result = userDBController.authenticateAndGetFullUserData(user);
				userDBController.setUserToLoggedIn(result);
				response = MessageParserUser.prepareMessageWithDataType_User(result,
						RequestType.SERVER_MESSAGE_TO_CLIENT_LOGIN_SUCCESS);
				sendMessageToGivenClient(response, client);
			} catch (BMServerException e) {
				response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
						e.getMessage());
				sendMessageToGivenClient(response, client);
			}
		} else if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_LOGOUT_REQUEST) {
			userDBController.setUserToLoggedOut(user);
		}
	}

	private void handleW4CRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		User user = MessageParserUser.handleMessageExtractDataType_User(msg);
		if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_W4C_REQUEST) {
			Object response;
			try {
				User result = userDBController.getCodesAccordingToW4C(user);
				response = MessageParserUser.prepareMessageWithDataType_User(result,
						RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
				sendMessageToGivenClient(response, client);
			} catch (BMServerException e) {
				response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
						e.getMessage());
				sendMessageToGivenClient(response, client);
			}
		}
	}

	private void handleRegisterClientRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		Client newClient = MessageParserBranchManager.handleMessageExtractDataType_Client(msg);
		System.out.println(newClient.getBusinessId());
		if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_CLIENT) {
			Object response;
			try {
				// newClient
				clientDBController.setNewClient(newClient);
				response = MessageParserBranchManager.prepareMessageWithResultOfRegisterion_Client(
						RequestType.SERVER_MESSAGE_TO_CLIENT_REGISTER_SUCCESS);
				sendMessageToGivenClient(response, client);
			} catch (BMServerException e) {
				response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
						e.getMessage());
				sendMessageToGivenClient(response, client);
			}
		}
	}

	private void handleRegisterSupplierRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		Supplier newSupplier = MessageParserBranchManager.handleMessageExtractDataType_Supplier(msg);
		if (actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_REGISTER_SUPPLIER) {
			Object response;
			try {
				supplierDBController.setNewSupplier(newSupplier);
				response = MessageParserBranchManager.prepareMessageWithResultOfRegisterion_Supplier(
						RequestType.SERVER_MESSAGE_TO_CLIENT_SUPPLIER_REGISTER_SUCCESS);
				sendMessageToGivenClient(response, client);
			} catch (BMServerException e) {
				response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
						e.getMessage());
				sendMessageToGivenClient(response, client);
			}
		}
	}
	private void handleGetDataOfBusiness(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		String branch = message.get(2);
		Object response;
			message = businessDBConteroller.getBusinessesNames(branch);
			if(message != null) {
			response = MessageParserBranchManager.prepareMessageWithResultOfGettingData_Business(RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_BUSINESSES_NAMES,message);
			sendMessageToGivenClient(response, client);
	
			}
		}
	
	private void handleApproveBusiness(RequestType actionRequired, Object msg, ConnectionToClient client) {
		Business business = MessageParserBranchManager.handleMessageExtractDataType_Business(msg);
		Object response;
		try {
			businessDBConteroller.approveBusiness(business);
			response = MessageParserBranchManager.prepareMessageWithResultOfApproveBusiness(
					RequestType.SERVER_MESSAGE_TO_CLIENT_APPROVE_BUSINESS_SUCCESS);
			sendMessageToGivenClient(response, client);
		} catch (BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}
		
	}
	private void handleGetDataOfClient(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		ArrayList<String> result;
		Object response;
		try {
			result = clientDBController.getClientInfo(message.get(2));
			response = MessageParserBranchManager.prepareMessageWithDataType_GetDataOfClient(result,actionRequired);
			sendMessageToGivenClient(response, client);
			
		} catch (BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}
		
	}
	
	private void handleChangePermission(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		Object response;
		
		try {
			clientDBController.changePermissionRequest(message.get(5),message.get(6));
			response = MessageParserBranchManager.prepareMessageWithResultOfChangePermission(
					RequestType.SERVER_MESSAGE_TO_CLIENT_CHANGE_PERMISSION_SUCCESS);
			sendMessageToGivenClient(response, client);
		} catch (BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}
		
		
	}
	
	private void handleOpenReportRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		String type = message.get(2);
		String year = message.get(3);
		String month = message.get(4);
		String path = message.get(5);
		String branch = message.get(6);
		Object response;
		try {
			String result = openReoprtDBController.openReoprt(type,year,month,path,branch);
			response = MessageParserTextString.prepareMessageWithDataType_SingleTextString(result,RequestType.SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS);
			sendMessageToGivenClient(response, client);
		}catch(BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}	
	}
	

	private void handleOpenIncomeReportForSupplier(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		String resIdString = message.get(2);
		int resId = Integer.parseInt(resIdString);
		String path = message.get(3);
		Object response;
		try {
			String result = openReoprtDBController.openIncomReoprtSupplier(resId,path);
			response = MessageParserTextString.prepareMessageWithDataType_SingleTextString(result,RequestType.SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS);
			sendMessageToGivenClient(response, client);
		}catch(BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}	
		
	}
	
	private void handleOpenReportFromBranch(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		String[] msgArr = message.get(2).split("\\s+");
		String[] date = msgArr[0].split("-");
		String year = date[0];
		String month = date[1];
		String branch = msgArr[1];
		String path = message.get(3);
		Object response;
		try {
			String result = openReoprtDBController.openReoprt("reportFromBranch",year,month,path,branch);
			response = MessageParserTextString.prepareMessageWithDataType_SingleTextString(result,RequestType.SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS);
			sendMessageToGivenClient(response, client);
		}catch(BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}
		
	}

	private void handleQuarterlyReportRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> message = (ArrayList<String>)msg;
		String quarter = message.get(2);
		String year = message.get(3);
		String path = message.get(4);
		Object response;
		try {
			String result = openReoprtDBController.openQuarterlyReport(quarter,year,path);
			response = MessageParserTextString.prepareMessageWithDataType_SingleTextString(result,RequestType.SERVER_MESSAGE_TO_CLIENT_OPEN_REPORT_SUCCESS);
			sendMessageToGivenClient(response, client);
		}catch(BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}	
		
	}
	

	private void handleSendReportToCEO(RequestType actionRequired, Object msg, ConnectionToClient client) {
		String message =  MessageParserTextString.handleMessageExtractDataType_SingleTextString(msg);
		String[] messageArr = message.split("branch");
		String fileString = messageArr[0];
		String branchName = messageArr[1];
		byte[] arr = Base64.getDecoder().decode(fileString);
		Object response;
		try {
			openReoprtDBController.saveFileForCEOInPdf(arr,branchName);
			response = MessageParserBranchManager.prepareMessageWithResultOfChangePermission(
					RequestType.SERVER_MESSAGE_TO_CLIENT_SEND_TO_CEO_SUCCESS);
		} catch (BMServerException e) {
			response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
					e.getMessage());
			sendMessageToGivenClient(response, client);
		}	
	}
	
	private void handleApproveBusinessRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        DataType messageDataType = MessageParser.parseMessage_DataType(msg);
        switch (messageDataType) {
            case HR_MANAGER:
                int hr_id = MessageParserHR.handleMessageFromClient_ApproveBusinessClient((ArrayList<String>) msg);
                HRDBController.approveBusiness(hr_id);
                break;
        }
    }
	
	private void handleCheckApproveBusinessRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        int isBusinessApproved = 0;
        ArrayList<String> message;
        DataType messageDataType = MessageParser.parseMessage_DataType(msg);
        switch (messageDataType) {
            case HR_MANAGER:
                int hr_id = MessageParserHR.handleMessageFromClient_ApproveBusinessClient((ArrayList<String>) msg);
                isBusinessApproved = HRDBController.CheckApproveBusiness(hr_id);
                message = MessageParserHR.prepareMessageToServer_HRCheckApproveBusiness(isBusinessApproved,messageDataType,actionRequired);
                sendMessageToGivenClient(message,client);
                break;

        }
    }
	
	private void handleSearchRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {		
		if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_SEARCH_RESTAURANT_REQUEST) {
			Object response;
    		try {
    			ArrayList<String> searchBranch = MessageParserTextString.parseMessageDataType_ArrayListString(msg);
    			ArrayList<Restaurant> result =  restaurantDBController.GetRestaurantsListFromSearchData(searchBranch);
            	response = MessegeParserRestaurants.prepareMessageWithDataType_Restaurants(result, RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
				sendMessageToGivenClient(response, client);

			} catch (BMServerException e) {
				response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(),
						e.getMessage());
				sendMessageToGivenClient(response, client);
			}
		}
	}
	
	private void handleFileIncomingRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		String fileName = MessageParserTextString.handleMessageExtractDataType_SingleTextString(msg);
		flagFileIncoming = true;
		fileSenderClient = client;
		System.out.println(fileName +"  "+ flagFileIncoming +"  "+ client.toString());
	}
	
	private void handleGetRestaurantsByCategoryRequest(RequestType actionRequired, Object msg,
			ConnectionToClient client) {
		ArrayList<String> category = MessageParserTextString.parseMessageDataType_ArrayListString(msg);

		if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST) {
			Object response;
    		try {
    			ArrayList<Restaurant> result =  restaurantDBController.GetRestaurantsListFromCategoriesData(category);
            	response = MessegeParserRestaurants.prepareMessageWithDataType_Restaurants(result, RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
            	sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
		}

	}

	private void handleGetMenuRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		String ResID = MessageParserTextString.handleMessageExtractDataType_SingleTextString(msg);

		if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_MENU_REQUEST) {
			Object response;
    		try {
    			ArrayList<Dish> result =  dishesDBController.GetDishesListFromResIDData(ResID);
            	response = MessegeParserDishes.prepareMessageWithDataType_Dishes(result, RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
            	sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
		}

	}
	
	private void handleGetBusinessClientData(RequestType actionRequired, Object msg, ConnectionToClient client) {
		if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_DATA) {
			String businessCode = MessageParserTextString.handleMessageExtractDataType_SingleTextString(msg);
			Object response;
    		try {
    			BusinessClientData result = budDBController.getBUD(businessCode);
            	response = MessageParserBusinessClientData.prepareMessageWithDataType_BusinessClientData(result, RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
            	sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
            	e.printStackTrace();
            	System.out.println(e.getMessage());
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
		}else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_BUSINESS_CLIENT_BUDGED_UPDATE) {
			ArrayList<String> updateRequest = MessageParserTextString.parseMessageDataType_ArrayListString(msg);
			budDBController.updateBudgetForClient(updateRequest);
		}
	}
	
	private void handleCRFRequest(RequestType actionRequired, Object msg, ConnectionToClient client){
		if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_CLIENT_REFUNDS_DATA) {
			ArrayList<String> resIDs = MessageParserTextString.parseMessageDataType_ArrayListString(msg);
			Object response;
			ClientRefundsData refundAmount =  orderDBController.getCRFDataForRestaurantsGiven(resIDs);
			//TODO: delete the prints
        	response = MessageParserClientRefundData.prepareMessageWithDataType_ClientRefundData(refundAmount, RequestType.SERVER_MESSAGE_TO_CLIENT_REFUND_AMOUNT);
        	sendMessageToGivenClient(response,client);
    	}
		else if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_REFUND_AMOUNT_AFTER_REFUNDS_USED) {
			ClientRefundsData crf = MessageParserClientRefundData.handleMessageExtractDataType_ClientRefundData(msg);
			orderDBController.updateCRFDataForRestaurantsGiven(crf);
		}
	}
	
	private void handleOrdersStatus(RequestType actionRequired, Object msg, ConnectionToClient client) {
        ArrayList<String> array = (ArrayList<String>) msg;
        Boolean response = orderDBController.moveOrder(array.get(2), array.get(3));
        if(response == false) {
            sendMessageToGivenClient(ErrorType.COULD_NOT_UPDATE_ORDER,client);
        }
    }
	
	private void handleCancelOrder(RequestType actionRequired, Object msg, ConnectionToClient client) {
        ArrayList<String> array = (ArrayList<String>) msg;
        Boolean response = orderDBController.cancelOrder(array.get(2));
        if(response == false) {
            sendMessageToGivenClient(ErrorType.COULD_NOT_CANCEL_ORDER,client);
        }
    }
	
	private void handleGetOrdersFromSupplierRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        ArrayList<String> data = (ArrayList<String>)msg;

        if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST) {
            Object response;
            try {
                ArrayList<Order> orders = orderDBController.getOrdersByResId(data.get(2), data.get(3));
                response = MessageParserOrder.prepareMessageWithDataType_Orders(orders, 
                        RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
                sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
        }

    }
	
	private void handleDeleteDishFromMenuRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        Dish dish = MessegeParserDishes.handleMessageExtractDataType_SingleDish(msg);

        if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_DELETE_DISH_FROM_MENU_REQUEST) {
            Object response;
            try {
                dishesDBController.DeleteDishFromMenu(dish);
                response = MessageParserTextString.prepareMessageWithDataType_SingleTextString("your dish deleted", 
                        RequestType.SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS);
                sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
        }

    }
	
	private void handleUpdateDishInMenuRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        Dish dish = MessegeParserDishes.handleMessageExtractDataType_SingleDish(msg);

        if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_UPDATE_DISH_IN_MENU_REQUEST) {
            Object response;
            try {
                dishesDBController.UpdateDishInMenu(dish);
                response = MessageParserTextString.prepareMessageWithDataType_SingleTextString("your dish updated", 
                        RequestType.SERVER_MESSAGE_TO_CLIENT_UPDATED_DISH_SUCCESS);
                sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
        }

    }
	
	private void handleAddDishToMenuRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        Dish dish = MessegeParserDishes.handleMessageExtractDataType_SingleDish(msg);

        if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_ADD_DISH_TO_MENU_REQUEST) {
            Object response;
            try {
                Dish result = dishesDBController.AddDishToMenu(dish);
                response = MessegeParserDishes.prepareMessageWithDataType_SingleDish(result,RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
                sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
        }

    }
	
	private void handleGetResBySupplierRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
        User supplier = MessageParserUser.handleMessageExtractDataType_User(msg);

        if(actionRequired == RequestType.CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST) {
            Object response;
            try {
                Restaurant result =  restaurantDBController.GetRestaurantFromUserSupplierData(supplier);
                response = MessegeParserRestaurants.prepareMessageWithDataType_singleRestaurant(result, RequestType.SERVER_MESSAGE_TO_CLIENT_DATA_PROVIDED);
                sendMessageToGivenClient(response,client);
            }catch(BMServerException e) {
                response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
                sendMessageToGivenClient(response,client);
            }
        }

    }
	


	private void handleGetReportFromCEO(RequestType actionRequired, Object msg, ConnectionToClient client) {
		ArrayList<String> result = new 	ArrayList<String>();
		Object response;
		try {
			result = openReoprtDBController.getBranchReportsFroCEO();
			response = MessageParserTextString.prepareMessageToServerDataType_ArrayListStringAllString(result,RequestType.SERVER_MESSAGE_TO_CLIENT_CEO_GET_BRANCHES_REPORTS);
			sendMessageToGivenClient(response,client);
		} catch (BMServerException e) {
			 response = MessageParserError.prepareMessageToClientWithDataType_Error(e.getErrorType(), e.getMessage());
             sendMessageToGivenClient(response,client);
		}
		
	}


    

	// -------------------------DEBUG FUNCTIONS
	private void handleDebugRequest(RequestType actionRequired, Object msg, ConnectionToClient client) {
		DataType messageDataType = MessageParser.parseMessage_DataType(msg);
		if (messageDataType == DataType.SINGLE_TEXT_STRING) {
			ArrayList<String> message = (ArrayList<String>) msg;
			System.out.println(actionRequired.toString() + " " + messageDataType.toString() + " " + message.get(2));
		}
	}

	// -----------------------UI FUNCTIONS
	public void setGuiController(ServerMainWindowController guiController) {
		this.guiController = guiController;
	}

	private void serverPrintToGuiLog(String message, boolean addPrintToTerminal) {
		guiController.updateDataLog(message);
		if (addPrintToTerminal)
			System.out.println(message);
	}

	private void serverPrintToGuiNumberOfClients(int numberOfClients) {
		guiController.updateNumberOfClientsConnected(numberOfClients);
	}
	
	///------------------------LAST MINUTE PANIC FUNCTIONS
	public DBController getDBController() {
		return dbController;
	}

}
